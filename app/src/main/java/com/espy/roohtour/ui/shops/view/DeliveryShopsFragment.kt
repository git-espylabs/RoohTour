package com.espy.roohtour.ui.shops.view

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.espy.roohtour.R
import com.espy.roohtour.api.Result
import com.espy.roohtour.api.models.shops.DeliveryShop
import com.espy.roohtour.databinding.FragmentDeliveryShopsBinding
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.shops.adapter.DeliveryShopListAdapter
import java.util.*
import androidx.navigation.fragment.navArgs
import com.espy.roohtour.api.models.shops.EnquiryAgencyItem
import java.text.SimpleDateFormat


class DeliveryShopsFragment:
    BaseFragmentWithBinding<FragmentDeliveryShopsBinding>(R.layout.fragment_delivery_shops) {

    private val shopsViewModel: ShopsViewModel by activityViewModels()
    private var deliveryShopListAdapter: DeliveryShopListAdapter? = null
    private val args: DeliveryShopsFragmentArgs by navArgs()
    private var searchList: List<EnquiryAgencyItem> = listOf()
    val DATE_FORMAT = "dd MMM yyyy"
    val DATE_FORMAT_REGULAR = "dd-MM-yyyy"
    val DATE_FORMAT_SERVER = "yyyy-MM-dd"
    var selectedDateFrom = "";
    var selectedDateTo = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedDateFrom = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(Calendar.getInstance().time)
        selectedDateTo = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(Calendar.getInstance().time)

        binding.apply {
            viewModel = shopsViewModel
            filterDateFrom.visibility = View.VISIBLE
            filterDateTo.visibility = View.VISIBLE
            tv1.visibility = View.VISIBLE
            btnShow.visibility = View.VISIBLE
            filterDateFrom.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Calendar.getInstance().time)
            filterDateTo.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Calendar.getInstance().time)
        }

        setObservers()

        (activity as OrderHistoryActivity).showProgress()
        shopsViewModel.getEnquiryAgencyList(selectedDateFrom, selectedDateTo)

        binding.filterDateTo.setOnClickListener {
            showDatePickerDialog(1)
        }

        binding.filterDateFrom.setOnClickListener {
            showDatePickerDialog(2)
        }

        binding.btnShow.setOnClickListener {
            (activity as OrderHistoryActivity).showProgress()
            shopsViewModel.getEnquiryAgencyList(selectedDateFrom, selectedDateTo)
        }
    }

    override fun onStop() {
        super.onStop()
        shopsViewModel._deliveryShopsList = MutableLiveData()
    }

    private fun setObservers(){
        shopsViewModel.enquiry_agency_List.observe(viewLifecycleOwner) {
            (activity as OrderHistoryActivity).hideProgress()
            if (it is Result.Success && it.data.any()) {
                loadOrders(it.data)
                setAutoCompleteSearch()
            } else {
                loadOrders(listOf())
                searchList = listOf()
                showToast("No shops found!")
            }
        }
    }

    private fun loadOrders(list: List<EnquiryAgencyItem>){
        deliveryShopListAdapter = DeliveryShopListAdapter(requireContext(), list){ shop, isNavigating ->
            if (isNavigating){
                openMapsNavigation((shop.lattitude?:"0.0").toDouble(), (shop.longitude?:"0.0").toDouble())
            }else{
                findNavController().navigate(DeliveryShopsFragmentDirections.actionDeliveryShopsFragmentToPendingOrderFragment(shop))
            }
        }.apply {
            notifyDataSetChanged()
        }

        binding.rvShopsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = deliveryShopListAdapter
            setHasFixedSize(false)
        }
    }

    private fun setAutoCompleteSearch(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, searchList)
        binding.srch.apply {
            threshold = 1
            setTextColor(Color.BLACK)
            setAdapter(adapter)

            setOnItemClickListener { parent, _, position, _ ->

                searchList.find {
                    it.shop_name == (parent.getItemAtPosition(position) as EnquiryAgencyItem).shop_name
                }?.run {
                    val tempList = arrayListOf<EnquiryAgencyItem>()
                    tempList.add(this)
                    loadOrders(tempList.toList())
                    (activity as OrderHistoryActivity).hideKeyboard()
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
                        binding.icSrch.setImageResource(R.drawable.ic_close_small)
                    }else{
                        binding.icSrch.setImageResource(R.drawable.ic_search)
                        loadOrders(searchList)
                    }
                }
            })
        }
    }

    private fun openMapsNavigation(latitude: Double, longitude: Double){
        val uri: String = java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        requireContext().startActivity(intent)
    }

    private fun showDatePickerDialog(dateType: Int){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        DatePickerDialog(requireActivity(), { _, year, monthOfYear, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            if (dateType == 1) {
                binding.filterDateTo.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(c.time)
                selectedDateTo = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
            } else {
                binding.filterDateFrom.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(c.time)
                selectedDateFrom = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
            }
        }, year, month, day).show()
    }
}