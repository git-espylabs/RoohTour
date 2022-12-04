package com.espy.roohtour.ui.shops.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.espy.roohtour.R
import com.espy.roohtour.api.models.shops.CheckData
import com.espy.roohtour.api.models.shops.Route
import com.espy.roohtour.app.AppPermission
import com.espy.roohtour.databinding.FragmentAddShopBinding
import com.espy.roohtour.extensions.handlePermission
import com.espy.roohtour.extensions.requestPermission
import com.espy.roohtour.location.GpsListener
import com.espy.roohtour.location.GpsManager
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.shops.models.NewShopData
import com.espy.roohtour.utils.CommonUtils
import java.io.File
import java.io.IOException

class AddShopFragment:
    BaseFragmentWithBinding<FragmentAddShopBinding>(R.layout.fragment_add_shop), GpsListener, View.OnClickListener {

    private val shopsViewModel: ShopsViewModel by activityViewModels()
    private var currentPhotoPath: String? = null
    private var absolutePhotoPathe: String? = null
    private var photoFile: File? = null
    private var shopLatitude = 0.0
    private var shopLongitude = 0.0
    private var shopRouteId = "0"
    private var categoryId = "0"
    var routesList = arrayListOf<Route>()
    var categoryList=CheckData.categoryList

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[AppPermission.PERMISSION_LOCATION[0]] == true && permissions[AppPermission.PERMISSION_LOCATION[1]] == true) {
                (activity as AddNewShopActivty).showProgress()
                GpsManager(this, requireActivity()).getLastLocation()
            } else {
                onLocationDisabled()
            }
        }


    var cameraLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            setCameraPicToImageView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = shopsViewModel
            viewParent = this@AddShopFragment
        }

        (activity as AddNewShopActivty).setToolBarProperties(getString(R.string.add_shop), isHome = false)

        setObserver()

        (activity as AddNewShopActivty).showProgress()
        shopsViewModel.getRouteList()

        binding.spnRoutes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                shopRouteId = routesList[position].id?:"0"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                shopRouteId = "0"
            }
        }


        //category

        binding.spnCategory.adapter = ArrayAdapter(requireContext(),
            R.layout.custom_spinner_text, R.id.text1, categoryList)

        binding.spnCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoryId = categoryList[position].id?:"0"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                categoryId = "0"
            }
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onLocationUpdate(location: Location?) {
        (activity as AddNewShopActivty).hideProgress()
        location?.let { loc ->
            shopLatitude = loc.latitude
            shopLongitude = loc.longitude

            binding.apply {
                /*etLat.text = "Lat: " + String.format("%.7f", loc.latitude)
                etLon.text = "Lon: " + String.format("%.7f", loc.longitude)*/
                etLat.text = getString(R.string.shop_Lat, loc.latitude)
                etLon.text = getString(R.string.shop_Lon, loc.longitude)
            }
        }
    }

    override fun onLocationDisabled() {
        (activity as AddNewShopActivty).hideProgress()
        showAlertDialog(R.string.location_disabled)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnFetchLoc ->{
                enquireLocationPermission()
            }
            R.id.userImg ->{
                dispatchTakePictureIntent()
            }
            R.id.capture ->{
                dispatchTakePictureIntent()
            }
            R.id.btnSubmit ->{
                if (isAllFieldsValid()){
                    showUploadWarning(shopData())
                }
            }
        }
    }

    private fun setObserver(){
        shopsViewModel.shopRoutes.observe(viewLifecycleOwner, {
            (activity as AddNewShopActivty).hideProgress()
            val route = Route("0", "Select Route")

            routesList = arrayListOf()
            routesList.add(route)
            routesList.addAll(it)

            binding.spnRoutes.adapter = ArrayAdapter(requireContext(),
                R.layout.custom_spinner_text, R.id.text1, routesList)

        })

        shopsViewModel.addNewShop.observe(viewLifecycleOwner, {
            (activity as AddNewShopActivty).hideProgress()
            if (it){
                showToast("Successfully added new Shop")
                (activity as AddNewShopActivty).onBackPressed()
            }else{
                showToast(R.string.something_went_wrong)
            }
        })
    }

    private fun shopData(): NewShopData{
        return NewShopData(
            binding.shName.text.toString(),
            binding.shRegNo.text.toString(),
            binding.shPrimaryNo.text.toString(),
            binding.shSecondryNO.text.toString(),
            binding.shAdd.text.toString(),
            shopLatitude.toString(),
            shopLongitude.toString(),
            binding.shEmail.text.toString(),
            shopRouteId,
            absolutePhotoPathe?:"",


            binding?.shortName.text.toString(),
            binding?.refNumber.text.toString(),
            binding?.emailTv.text.toString(),
            binding?.contactPersonTv.text.toString(),

            categoryId

        )
    }

    private fun showUploadWarning(newShopData: NewShopData) {
        AlertDialog.Builder(requireContext())
            .setMessage("Confirm submit new Agency details?")
            .setPositiveButton("Yes") { dialog, _ ->
                (activity as AddNewShopActivty).showProgress()
                shopsViewModel.uploadNewShop(newShopData, requireContext())
                dialog.dismiss()
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun enquireLocationPermission(){
        handlePermission(AppPermission.ACCESS_FINE_LOCATION,
            onGranted = {
                (activity as AddNewShopActivty).showProgress()
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

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            try {
                photoFile = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireActivity(),
                        CommonUtils.getFileProviderName(requireContext()),
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    cameraLaucher.launch(takePictureIntent)
                }
            } catch (_: Exception) {

            }
        }
    }

    private fun createImageFile(): File {
        return CommonUtils.createImageFile(requireContext(),
            "USER"
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun setCameraPicToImageView() {
        scaleDownImage(BitmapFactory.decodeFile(currentPhotoPath))
        deleteOriginalCameraImage()
    }

    private fun scaleDownImage(image: Bitmap) {
        with(CommonUtils.scaleDownImage(image)) {
            CommonUtils.compressAndSaveImage(requireContext(), this, "USER").also {
                absolutePhotoPathe = it.absolutePath
                setImage(it.absolutePath)
            }
        }
    }

    private fun deleteOriginalCameraImage() {
        try {
            photoFile?.delete()
        } catch (_: Exception) {
        }
        photoFile = null
    }

    private fun setImage(path: String){
        val image = BitmapFactory.decodeFile(path)
        image?.let {
            binding.userImg.apply {
                setImageBitmap(image)
                scaleType = ImageView.ScaleType.CENTER_CROP
                setPadding(0, 0, 0, 0)
            }
        }
    }

    private fun isAllFieldsValid(): Boolean{
        return binding.run {
            when{
                /*shRegNo.text.toString().isEmpty() ->{
                    showToast("Please enter shop register number")
                    shRegNo.error = "Register Number"
                    false
                }*/
                shName.text.toString().isEmpty() ->{
                    showToast("Please enter name")
                    shName.error = "Name"
                    false
                }
///////////////////////////////////////
                shortName.text.toString().isEmpty() ->{
                    showToast("Please enter Short name")
                    shName.error = "Short Name"
                    false
                }

                refNumber.text.toString().isEmpty() ->{
                    showToast("Please enter ref number")
                    shName.error = "Reference Name"
                    false
                }

                emailTv.text.toString().isEmpty() ->{
                    showToast("Please enter email")
                    shName.error = "Email"
                    false
                }

                contactPersonTv.text.toString().isEmpty() ->{
                    showToast("Please enter contact person name")
                    shName.error = "Contact Person Name"
                    false
                }

                (categoryId == "0") ->{
                    showToast("Please select a category")
                    false
                }

                /////////////////////////////////////////////
                shPrimaryNo.text.toString().isEmpty() ->{
                    showToast("Please enter primary number")
                    shPrimaryNo.error = "Primary Number"
                    false
                }
                shAdd.text.toString().isEmpty() ->{
                    showToast("Please enter address")
                    shAdd.error = "Shop Address"
                    false
                }
                /*shEmail.text.toString().isEmpty() ->{
                    showToast("Please enter email")
                    shEmail.error = "Shop Email"
                    false
                }*/
                (shopLatitude == 0.0 || shopLongitude ==0.0) ->{
                    showToast("Shop location is mandatory")
                    false
                }
                (shopRouteId == "0") ->{
                    showToast("Please select a route to continue")
                    false
                }
                else ->{
                    true
                }
            }
        }
    }
}