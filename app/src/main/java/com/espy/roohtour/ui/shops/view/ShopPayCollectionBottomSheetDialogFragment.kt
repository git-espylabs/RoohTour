package com.espy.roohtour.ui.shops.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.espy.roohtour.R
import com.espy.roohtour.databinding.FragmentShopPayCollectionBottomSheetBinding
import com.espy.roohtour.extensions.dpToPixel
import com.espy.roohtour.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ShopPayCollectionBottomSheetDialogFragment(var viewModel: ShopsViewModel, var shopId: String) :
    BottomSheetDialogFragment()   {

    private lateinit var binding: FragmentShopPayCollectionBottomSheetBinding
    val DATE_FORMAT = "dd MMM yyyy"
    private var currentPhotoPath: String? = null
    private var absolutePhotoPathe: String? = null
    private var photoFile: File? = null
    val DATE_FORMAT_SERVER = "yyyy-MM-dd"
    var selectedChequeDate = "";


    var cameraLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            setCameraPicToImageView()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shop_pay_collection_bottom_sheet,
            container,
            false
        )
        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            outstandingAmt.text = requireContext().getString(R.string.amount_rep_float, viewModel.shopsOutstandingTotal)

            spnPayTyp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (parent.selectedItem.toString().equals("Cheque", true)){
                        cheqDataGrp.visibility = View.VISIBLE
                    }else{
                        cheqDataGrp.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }

            if (viewModel.capPaymentTypes.isNotEmpty()){
                spnPayTyp.adapter = ArrayAdapter(requireContext(),
                    android.R.layout.simple_spinner_dropdown_item, viewModel.capPaymentTypes)
            }else{
                spnPayTyp.adapter = ArrayAdapter(requireContext(),
                    android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.pay_types))
            }

            btnSubmit.setOnClickListener {
                if (isAllFieldsValid()){
                    viewModel.submitPaymentCollection(binding.etAmount.text.toString(),
                        (spnPayTyp.selectedItemPosition+1).toString(), absolutePhotoPathe?:"", requireContext(), shopId, etChecqNum.text.toString(), etChecqDate.text.toString())
                    dismiss()
                }
            }

            etDate.setOnClickListener {
                showDatePickerDialog(1)
            }

            btnClose.setOnClickListener {
                dismiss()
            }

            capture.setOnClickListener {
                dispatchTakePictureIntent()
            }

            userImg.setOnClickListener {
                dispatchTakePictureIntent()
            }

            etChecqDate.setOnClickListener {
                showDatePickerDialog(2)
            }
        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()

        val tv = TypedValue()
        val normalActionBarHeight = 56
        var actionBarHeight = normalActionBarHeight.dpToPixel().toInt()
        if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }

        if (layoutParams != null) {
            layoutParams.height = windowHeight - actionBarHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    private fun showDatePickerDialog(dateType: Int){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val datePicker = DatePickerDialog(requireActivity(), { _, year, monthOfYear, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            if (dateType == 1) {
                binding.etDate.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(c.time)
            } else {
                binding.etChecqDate.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(c.time)
                selectedChequeDate = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
            }
        }, year, month, day)

        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
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
            when {
                etAmount.text.toString().isEmpty() -> {
                    (activity as ShopDetailsActivity).showToast("Please enter an amount")
                    etAmount.error = "Enter an amount"
                    false
                }
                (spnPayTyp.selectedItem.toString().equals("Cheque", true) && (etChecqDate.text.toString().isNullOrEmpty() || etChecqNum.text.toString().isNullOrEmpty())) ->{
                    Toast.makeText(requireContext(), "Cheque Number & Date are required", Toast.LENGTH_SHORT).show()
                    false
                }
                /*(null == currentPhotoPath || currentPhotoPath?.isEmpty() == true) -> {
                    (activity as ShopDetailsActivity).showToast("Please capture a photo")
                    false
                }*/
                else -> {
                    true
                }
            }
        }
    }
}