package com.espy.roohtour.ui.attendance.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.espy.roohtour.R
import com.espy.roohtour.app.AppPermission
import com.espy.roohtour.databinding.FragmentAttendaceBinding
import com.espy.roohtour.extensions.handlePermission
import com.espy.roohtour.extensions.requestPermission
import com.espy.roohtour.extensions.setDrawableTint
import com.espy.roohtour.interfaces.PhotoOptionListener
import com.espy.roohtour.location.GpsListener
import com.espy.roohtour.location.GpsManager
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.utils.CommonUtils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AttendanceFragment:
    BaseFragmentWithBinding<FragmentAttendaceBinding>(R.layout.fragment_attendace),
    View.OnClickListener,
    PhotoOptionListener,
    GpsListener{

    private lateinit var attendanceViewModel: AttendanceViewModel
    private val TIME_FORMAT = "dd MMMM, hh:mm a"
    private var currentPhotoPath = ""
    private var actualPath = ""
    private var photoFile: File? = null
    private val STATUS_PUNCH_IN = "0"
    private val STATUS_PUNCH_OUT = "1"
    private var status = STATUS_PUNCH_IN


    private var cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            setCameraPicToImageView()
        }
    }

    private var galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let {
                setGalleryPicToImageView(it)
            }
        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[AppPermission.PERMISSION_LOCATION[0]] == true && permissions[AppPermission.PERMISSION_LOCATION[1]] == true) {
                (activity as AttendanceActivity).showProgress()
                GpsManager(this, requireActivity()).getLastLocation()
            } else {
                (activity as AttendanceActivity).showProgress()
                onLocationDisabled()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        attendanceViewModel = ViewModelProvider(this).get(AttendanceViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = attendanceViewModel
            viewParent = this@AttendanceFragment
        }

        showCurrentTime()

        attendanceViewModel.attendanceMarked.observe(viewLifecycleOwner) {
            (activity as AttendanceActivity).hideProgress()
            showToast(it)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.punchinlay -> {
                status = STATUS_PUNCH_IN
                enquireLocationPermission()
            }
            R.id.punchoutlay -> {
                status = STATUS_PUNCH_OUT
                enquireLocationPermission()
            }
            R.id.capture -> {
                dispatchTakePictureIntent()
            }
        }
    }

    override fun onTakePhotoSelected() {
        dispatchTakePictureIntent()
    }

    override fun onChoosePhotoSelected() {
        dispatchPickPhotoIntent()
    }

    override fun onLocationUpdate(location: Location?) {
        processPunchInPunchOut(location)
    }

    override fun onLocationDisabled() {
        processPunchInPunchOut(null)
    }

    private fun enquireLocationPermission(){
        handlePermission(AppPermission.ACCESS_FINE_LOCATION,
            onGranted = {
                (activity as AttendanceActivity).showProgress()
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

    private fun togglePunchViews(isSelected: Boolean, textView: TextView, layout: LinearLayout){
        if (isSelected) {
            layout.background = CommonUtils.getDrawable(requireContext(), R.drawable.bg_cap_button)
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            textView.setDrawableTint(R.color.white)
        } else {
            layout.background = CommonUtils.getDrawable(requireContext(), R.drawable.bg_orange_border_white_filled_curver_corner_rect)
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            textView.setDrawableTint(R.color.app_accent_color)
        }
    }

    private fun showCurrentTime(){
        val dateTime = SimpleDateFormat(TIME_FORMAT).format(Calendar.getInstance().time)
        binding.tvDate.text = dateTime
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
                    cameraLauncher.launch(takePictureIntent)
                }
            } catch (_: Exception) {
                showToast(R.string.no_apps_found)
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
            showToast(R.string.no_apps_found)
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
        currentPhotoPath = uri.path?: ""
        photoFile = File(currentPhotoPath)
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

    private fun processPunchInPunchOut(location: Location?){
        when(status){
            STATUS_PUNCH_IN ->{
                binding.apply {
                    if (actualPath.isNullOrEmpty().not()) {
                        attendanceViewModel.processPunchInPunchOut("0", actualPath, requireContext())
                        togglePunchViews(isSelected = true, textView = punch, punchinlay)
                        togglePunchViews(isSelected = false, textView = punchout, punchoutlay)
                    } else {
                        (activity as AttendanceActivity).hideProgress()
                        showToast("Please choose/capture an image to upload!")
                    }
                }
            }
            STATUS_PUNCH_OUT ->{
                binding.apply {
                    if (actualPath.isNullOrEmpty().not()) {
                        (activity as AttendanceActivity).showProgress()
                        attendanceViewModel.processPunchInPunchOut("1", actualPath, requireContext())
                        togglePunchViews(isSelected = true, textView = punchout, punchoutlay)
                        togglePunchViews(isSelected = false, textView = punch, punchinlay)
                    } else {
                        showToast("Please choose/capture an image to upload!")
                    }
                }
            }
        }
    }



    private fun showPhotoPickOption() {
        PhotoOptionBottomSheetDialogFragment(this@AttendanceFragment).show(
            childFragmentManager,
            "ChoosePhotoFragment"
        )
    }




}