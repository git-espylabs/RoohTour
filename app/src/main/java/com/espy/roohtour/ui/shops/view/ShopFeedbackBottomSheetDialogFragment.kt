package com.espy.roohtour.ui.shops.view

import android.app.Activity
import android.app.Dialog
import android.location.Location
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.espy.roohtour.R
import com.espy.roohtour.databinding.FragmentShopFeedbackBottomSheetBinding
import com.espy.roohtour.extensions.dpToPixel
import com.espy.roohtour.location.GpsListener
import com.espy.roohtour.location.GpsManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShopFeedbackBottomSheetDialogFragment(var viewModel: ShopsViewModel) :
    BottomSheetDialogFragment(), GpsListener {

    private lateinit var binding: FragmentShopFeedbackBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_shop_feedback_bottom_sheet,
            container,
            false
        )
        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            btnSubmit.setOnClickListener {
                if (isAllFieldsValid()){
                    GpsManager(this@ShopFeedbackBottomSheetDialogFragment, requireActivity()).getLastLocation()
                }else{
                    (activity as ShopDetailsActivity).showToast("All fields are mandatory")
                    etTitle.error = "Please enter a title"
                    etDesc.error = "Please enter a description"
                }
            }

            btnClose.setOnClickListener {
                dismiss()
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

    override fun onLocationUpdate(location: Location?) {
        viewModel.postFeedBack(binding.etTitle.text.toString(), binding.etDesc.text.toString(), location)
        dismiss()
    }

    override fun onLocationDisabled() {
        viewModel.postFeedBack(binding.etTitle.text.toString(), binding.etDesc.text.toString(), null)
        dismiss()
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

    private fun isAllFieldsValid(): Boolean{
        return binding.run {
            etTitle.text.toString().isEmpty().not() && etDesc.text.toString().isEmpty().not()
        }
    }
}