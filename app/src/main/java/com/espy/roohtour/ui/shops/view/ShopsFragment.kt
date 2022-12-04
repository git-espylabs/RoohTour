package com.espy.roohtour.ui.shops.view

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.espy.roohtour.R
import com.espy.roohtour.api.models.shops.Shop
import com.espy.roohtour.app.AppPermission
import com.espy.roohtour.app.InstanceManager
import com.espy.roohtour.databinding.FragmentShopsBinding
import com.espy.roohtour.extensions.handlePermission
import com.espy.roohtour.extensions.launchActivity
import com.espy.roohtour.extensions.requestPermission
import com.espy.roohtour.location.GpsListener
import com.espy.roohtour.location.GpsManager
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.home.HomeActivity
import com.espy.roohtour.ui.home.HomeViewModel
import com.espy.roohtour.ui.shops.adapter.ShopsListAdapter
import android.content.Intent
import android.net.Uri


class ShopsFragment:
    BaseFragmentWithBinding<FragmentShopsBinding>(R.layout.fragment_shops), GpsListener{

    private val homeViewModel: HomeViewModel by activityViewModels()
    private var shopsListAdapter: ShopsListAdapter? = null
    private lateinit var selectedShop: Shop
    private var statusInOut = -1
    private var isAlreadyShoppedIn = false


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[AppPermission.PERMISSION_LOCATION[0]] == true && permissions[AppPermission.PERMISSION_LOCATION[1]] == true) {
                (activity as HomeActivity).showProgress()
                GpsManager(this, requireActivity()).getLastLocation()
            } else {
                (activity as HomeActivity).showProgress()
                onLocationDisabled()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = homeViewModel
        }

        (activity as HomeActivity).setToolBarProperties(getString(R.string.shops), isHome = false)

        setObservers()

        homeViewModel.getShopsList()

        binding.iconsearch.setOnClickListener {
            binding.etSearch.setText("")
            loadShops(InstanceManager.shopsList)
        }
    }

    override fun onResume() {
        super.onResume()
        shopsListAdapter?.apply {
            notifyDataSetChanged()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.actionLogout).isVisible = false
        menu.findItem(R.id.actionSearch).isVisible = false
    }

    override fun onLocationUpdate(location: Location?) {
        if (location != null && location.latitude != 0.0 && location.longitude != 0.0){
            processShopInOut(location)
        }else{
            onLocationDisabled()
        }
    }

    override fun onLocationDisabled() {
        (activity as HomeActivity).hideProgress()
        showAlertDialog(R.string.alert_location_off)
    }

    private fun setObservers(){

        homeViewModel.isShoppedIn.observe(viewLifecycleOwner) {
            (activity as HomeActivity).hideProgress()
            isAlreadyShoppedIn = true
            if (it != null) {
                homeViewModel.emptyCart()
                AppPreferences.shopInId = it.id ?: "0"
                AppPreferences.orderDiscount = 0.0
                shopsListAdapter?.apply {
                    notifyDataSetChanged()
                }
                enterShop(it)
            } else {
                showToast(R.string.already_shopin_warning)
            }
        }

        homeViewModel.isShoppedOut.observe(viewLifecycleOwner) {
            (activity as HomeActivity).hideProgress()
            if (it) {
                AppPreferences.shopInId = ""
                isAlreadyShoppedIn = true
                shopsListAdapter?.apply {
                    notifyDataSetChanged()
                }
            } else {
                showToast(R.string.something_went_wrong)
            }
        }

        homeViewModel.allShops.observe(viewLifecycleOwner) {
            InstanceManager.shopsList = it
            if (InstanceManager.shopsList.isNotEmpty()) {
                loadShops(InstanceManager.shopsList)
                setAutoCompleteSearch()
            } else {
                showToast(R.string.no_shops_found)
            }
        }
    }

    private fun loadShops(list: List<Shop>){

        val clickHandler: (shop: Shop, isPromptingInOutAction: Boolean) -> Unit = { shop, isPromptingInOutAction ->
            selectedShop = shop
            if (AppPreferences.shopInId.isNotEmpty() && AppPreferences.shopInId == shop.id && isPromptingInOutAction.not()) {
                statusInOut = -1
                enterShop(shop)
            }
            else if (AppPreferences.shopInId.isNotEmpty() && AppPreferences.shopInId == shop.id && isPromptingInOutAction){
                statusInOut = 1
                enquireLocationPermission()
            }else if (AppPreferences.shopInId.isEmpty() && AppPreferences.shopInId != shop.id && isPromptingInOutAction){
                statusInOut = 0
                enquireLocationPermission()
            }else if (AppPreferences.shopInId.isNotEmpty() && AppPreferences.shopInId != shop.id && isPromptingInOutAction){
                showAlertDialog(R.string.already_shopin_warning)
            }
        }

        val callHandler: (phoneNumber: String) -> Unit = { phoneNumber ->
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }

        shopsListAdapter = ShopsListAdapter(
            requireContext(),
            list,
            clickHandler,
            callHandler
        ).apply {
            notifyDataSetChanged()
        }

        binding.shoplist.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shopsListAdapter
            setHasFixedSize(false)
        }
    }

    private fun enterShop(shop: Shop){
        activity?.launchActivity<ShopDetailsActivity> {
            this.putExtra(
                ShopDetailsActivity.SHOP_ARGS,
                shop
            )
        }
    }

    private fun enquireLocationPermission(){
        handlePermission(AppPermission.ACCESS_FINE_LOCATION,
            onGranted = {
                (activity as HomeActivity).showProgress()
                GpsManager(this, requireActivity()).getLastLocation()
            },
            onDenied = {
                requestPermission(requestPermissionLauncher, AppPermission.PERMISSION_LOCATION)
            },
            onRationaleNeeded = {
                Log.d("DEBUG", "Permission onRationaleNeeded")
            }
        )
    }

    private fun processShopInOut(location: Location?){
        when(statusInOut){
            1 ->{
                homeViewModel.tagShopOut(selectedShop.id?:"0", location)
            }
            0 ->{
                homeViewModel.tagShopIn(selectedShop, location)
            }
        }
    }

    private fun setAutoCompleteSearch(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, InstanceManager.shopsList)
        binding.etSearch.apply {
            threshold = 1
            setTextColor(Color.BLACK)
            setAdapter(adapter)

            setOnItemClickListener { parent, _, position, _ ->

                InstanceManager.shopsList.find {
                    it.id == (parent.getItemAtPosition(position) as Shop).id
                }?.run {
                    val tempList = arrayListOf<Shop>()
                    tempList.add(this)
                    loadShops(tempList.toList())
                    (activity as HomeActivity).hideKeyboard()
                }
            }

            addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    if (s.toString().isNotEmpty()){
                        binding.iconsearch.setImageResource(R.drawable.ic_close_small)
                    }else{
                        binding.iconsearch.setImageResource(R.drawable.ic_search)
                        loadShops(InstanceManager.shopsList)
                    }
                }
            })
        }
    }
}