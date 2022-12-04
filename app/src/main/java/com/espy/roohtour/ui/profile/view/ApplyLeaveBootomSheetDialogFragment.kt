package com.espy.roohtour.ui.profile.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.espy.roohtour.R
import com.espy.roohtour.databinding.FragmenntApplyLeaveBottomSheetBinding
import com.espy.roohtour.extensions.dpToPixel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class ApplyLeaveBottomSheetDialogFragment(var viewModel: ProfileViewModel) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmenntApplyLeaveBottomSheetBinding
    val DATE_FORMAT = "dd MMM yyyy"
    val DATE_FORMAT_SERVER = "yyyy-MM-dd"
    var selectedDate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragmennt_apply_leave_bottom_sheet,
            container,
            false
        )

        binding.apply {
            btnClose.setOnClickListener {
                dismiss()
            }

            btnSubmit.setOnClickListener {
                if (isAllFieldsValid()){
                    processLeaveRequest()
                }
            }

            etDate.setOnClickListener {
                showDatePickerDialog()
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

    private fun showDatePickerDialog(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        DatePickerDialog(requireActivity(), { _, year, monthOfYear, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            binding.etDate.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(c.time)
            selectedDate = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
        }, year, month, day).show()
    }

    private fun isAllFieldsValid(): Boolean{
        binding.apply {
            return when {
                etDate.text.isEmpty() -> {
                    etDate.error = "Please choose a date"
                    false
                }
                etRemarks.text.toString().isEmpty() -> {
                    etRemarks.error = "Please enter remarks"
                    false
                }
                etDesc.text.toString().isEmpty() -> {
                    etDesc.error = "Please enter description"
                    false
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun processLeaveRequest(){
        val date = selectedDate
        val reason = binding.etDesc.text.toString()
        val remarks = binding.etRemarks.text.toString()

        viewModel.applyLeave(date, remarks, reason)
        dismiss()
    }

}