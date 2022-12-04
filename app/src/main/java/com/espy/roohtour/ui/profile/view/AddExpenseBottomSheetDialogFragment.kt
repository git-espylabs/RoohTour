package com.espy.roohtour.ui.profile.view

import android.app.Activity
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
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.espy.roohtour.R
import com.espy.roohtour.app.InstanceManager
import com.espy.roohtour.databinding.FragmentAddExpenseBottomSheetBinding
import com.espy.roohtour.extensions.dpToPixel
import com.espy.roohtour.ui.home.HomeViewModel
import com.espy.roohtour.utils.CommonUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.IOException


class AddExpenseBottomSheetDialogFragment<T : Any>(var viewModel: T) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddExpenseBottomSheetBinding
    private var currentPhotoPath = ""
    private var actualPath = ""
    private var photoFile: File? = null


    var cameraLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            setCameraPicToImageView()
        }
    }

    var galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let {
                setGalleryPicToImageView(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_expense_bottom_sheet,
            container,
            false
        )
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewParent = this@AddExpenseBottomSheetDialogFragment
        }

        setClickListeners()

        if (InstanceManager.capExpenseTypes.isNotEmpty()){
            binding.spnExpenseTyp.adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, InstanceManager.capExpenseTypes)
        }else{
            binding.spnExpenseTyp.adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.expense_types))
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

    private fun showImageUploadChooser() {
        AlertDialog.Builder(requireContext())
            .setMessage("Choose how do you want to upload")
            .setPositiveButton(getString(R.string.take_photo)) { _, _ ->
                dispatchTakePictureIntent()
            }
            .setNegativeButton(
                getString(R.string.choose_photo)
            ) { _, _ ->
                dispatchPickPhotoIntent()
            }
            .show()
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

    private fun dispatchPickPhotoIntent() {
        try {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            galleryLauncher.launch(intent)
        } catch (_: Exception) {
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

    private fun setGalleryPicToImageView(uri: Uri) {
        CommonUtils.getBitmapFromUri(requireContext(), uri)
            ?.let { bitmap -> scaleDownImage(bitmap) }
    }

    private fun scaleDownImage(image: Bitmap) {
        with(CommonUtils.scaleDownImage(image)) {
            CommonUtils.compressAndSaveImage(requireContext(), this, "USER").also {
                actualPath = it.absolutePath
                setImage(it.absolutePath)
            }
        }
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

    private fun deleteOriginalCameraImage() {
        try {
            photoFile?.delete()
        } catch (_: Exception) {
        }
        photoFile = null
    }

    private fun setClickListeners(){
        binding.apply {
            btnClose.setOnClickListener {
                dismiss()
            }

            userImg.setOnClickListener {
                dispatchTakePictureIntent()
            }

            capture.setOnClickListener {
                dispatchTakePictureIntent()
            }

            btnSubmit.setOnClickListener {
                if (isAllFieldsValid()){
                    if (viewModel is ProfileViewModel){
                        (viewModel as ProfileViewModel).submitExpense(
                            type_id = (spnExpenseTyp.selectedItemPosition+1).toString(),
                            amount = binding.etAmount.text.toString(),
                            note = binding.etDescription.text.toString(),
                            imagepath = actualPath,
                            context = requireContext()
                        )
                    }else if (viewModel is HomeViewModel){
                        (viewModel as HomeViewModel).submitExpense(
                            type_id = (spnExpenseTyp.selectedItemPosition+1).toString(),
                            amount = binding.etAmount.text.toString(),
                            note = binding.etDescription.text.toString(),
                            imagepath = actualPath,
                            context = requireContext()
                        )
                    }

                    dismiss()
                }
            }
        }
    }


    private fun isAllFieldsValid(): Boolean{
        return binding.run {
            when {
                etDescription.text.toString().isEmpty() -> {
                    etDescription.error = "Enter a description"
                    false
                }
                (currentPhotoPath.isEmpty() || actualPath.isEmpty()) -> {
                    false
                }
                etRemarks.text.toString().isEmpty() -> {
                    etRemarks.error = "Enter remarks"
                    false
                }
                etAmount.text.toString().isEmpty() -> {
                    etAmount.error = "Enter an amount"
                    false
                }
                else -> {
                    true
                }
            }
        }
    }
}