package com.espy.roohtour.ui.shops.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.espy.roohtour.R
import com.espy.roohtour.api.Result
import com.espy.roohtour.app.AppSettings
import com.espy.roohtour.databinding.FragmetShopDetailsBinding
import com.espy.roohtour.extensions.sumBy
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.api.models.shops.Shop
import com.espy.roohtour.api.models.shops.ShopOutstanding
import com.espy.roohtour.location.GpsListener
import com.espy.roohtour.location.GpsManager
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import java.text.SimpleDateFormat
import java.util.*

class ShopDetailsFragment :
    BaseFragmentWithBinding<FragmetShopDetailsBinding>(R.layout.fragmet_shop_details),
    View.OnClickListener, GpsListener {

    private lateinit var viewModel: ShopsViewModel
    val DATE_FORMAT = "dd MMM yyyy"
    val DATE_FORMAT_SERVER = "yyyy-MM-dd"
    var selectedDateofTravel = "";
    var selectedReminderDate = "";

    val args: ShopDetailsFragmentArgs by navArgs()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddSalesreturn -> {
                ShopSalesReturnBottomSheetDialog().show(
                    childFragmentManager,
                    "ShopSalesReturnFragment"
                )

            }
            R.id.collectPay -> {
                if (viewModel.shopsOutstandingTotal > 0) {
                    ShopPayCollectionBottomSheetDialogFragment(
                        viewModel,
                        args.shop?.id ?: "0"
                    ).show(
                        childFragmentManager,
                        "ShopPayCollectionFragment"
                    )
                } else {
                    showToast("No outstanding payments for this shop")
                }
            }
            R.id.btnStartOrder -> {
                /*  activity?.launchActivity<OrderActivity> {
                      putExtra(OrderActivity.ARGS_SHOP_ID, args.shop?.id?:0)
                  }*/
                showEnquiryDialog()

            }
            R.id.shopout -> {
                (activity as ShopDetailsActivity).showProgress()
                GpsManager(this, requireActivity()).getLastLocation()
            }
            R.id.payHistory -> {
                findNavController().navigate(ShopDetailsFragmentDirections.actionShopDetailsFragmentToShopCollectionHistoryFragment())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ShopsViewModel::class.java)

        binding.apply {
            viewModel = this.viewModel
            viewParent = this@ShopDetailsFragment
        }
        (activity as ShopDetailsActivity).setToolBarProperties(getString(R.string.shop_details))

        setObserver()
    }

    override fun onResume() {
        super.onResume()

        args.shop?.run {
            ShopDetailsActivity.currentShop = this
            populateShopDetails(this)
            id?.let {
                (activity as ShopDetailsActivity).showProgress()
                viewModel.getShopOutstanding(it)
            }
        }
    }

    override fun onLocationUpdate(location: Location?) {
        if (location != null && location.latitude != 0.0 && location.longitude != 0.0) {
            viewModel.tagShopOut(args.shop?.id ?: "0", location)
        } else {
            onLocationDisabled()
        }
    }

    override fun onLocationDisabled() {
        (activity as ShopDetailsActivity).hideProgress()
        showAlertDialog(R.string.alert_location_off)
    }

    private fun populateShopDetails(data: Shop?) {
        data?.run {
            binding.apply {
                Glide
                    .with(requireContext())
                    .load(AppSettings.endPoints.IMAGE_ASSETS + data.shop_image)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(shopImg)

                shopName.text = shop_name

                shopAddress.text = shop_address

                shopTime.text = ""

                shRoute.text = "Route: $route_name"

                try {
                    amountOutstanding.text = requireContext().getString(
                        R.string.amount_rep_float,
                        (shop_oustanding_amount ?: "0.00").toFloat()
                    )

                } catch (e: Exception) {
                }
            }
        }
    }

    private fun setObserver() {
        viewModel.shopsOutstandingList.observe(viewLifecycleOwner) {
            (activity as ShopDetailsActivity).hideProgress()
            if (it is Result.Success && it.data.any()) {
                calculateAndSetOutstandingTotal(it.data)
            }
        }

        viewModel.paymentTypesList.observe(viewLifecycleOwner) {
            if (it is Result.Success && it.data.any()) {
                viewModel.capPaymentTypes = it.data
            }
        }

        viewModel.paymentStatus.observe(viewLifecycleOwner) {
            if (it) {
                args.shop?.id?.let { shopid ->
                    (activity as ShopDetailsActivity).showProgress()
                    viewModel.getShopOutstanding(shopid)
                }
                showToast("Payment Completed. Thanks!")
            } else {
                showToast(getString(R.string.something_went_wrong))
            }
        }

        viewModel.isShoppedOut.observe(viewLifecycleOwner) {
            (activity as ShopDetailsActivity).hideProgress()
            if (it) {
                AppPreferences.shopInId = ""
                (activity as ShopDetailsActivity).onBackPressed()
            } else {
                showToast(R.string.something_went_wrong)
            }
        }
    }

    private fun calculateAndSetOutstandingTotal(list: List<ShopOutstanding>) {
        try {
            list.sumBy { (it.shop_oustanding_amount ?: "0.00").toFloat() }.apply {
                binding.amountOutstanding.text =
                    requireContext().getString(R.string.amount_rep_float, this)
                viewModel.shopsOutstandingTotal = this
                if (this <= 0) {
                    showToast("No outstanding payments for this shop")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showEnquiryDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.addenquirydialogsview)
        val title = dialog.findViewById(R.id.title) as TextView
        title.text = "Add Enquiry"
        val tv_close = dialog.findViewById(R.id.tv_close) as TextView

        //views
        val edt_destination = dialog.findViewById(R.id.edt_destination) as EditText
        val edt_date_of_travel = dialog.findViewById(R.id.edt_date_of_travel) as TextView

        val edt_comment = dialog.findViewById(R.id.edt_comment) as EditText
        val edt_adult = dialog.findViewById(R.id.edt_adult) as EditText

        val edt_child = dialog.findViewById(R.id.edt_child) as EditText
        val edt_duration = dialog.findViewById(R.id.edt_duration) as EditText

        val edt_quotation_amt = dialog.findViewById(R.id.edt_quotation_amt) as EditText
        val edt_reminder_date = dialog.findViewById(R.id.edt_reminder_date) as TextView

        val edt_notes = dialog.findViewById(R.id.edt_notes) as EditText

        val btnSubmit = dialog.findViewById(R.id.btnSubmit) as Button


        btnSubmit.setOnClickListener {
//validation
            if (edt_destination.text.trim().toString().isEmpty()) {
                makeToast("Enter Destination")
            } else if (edt_date_of_travel.text.trim().toString().isEmpty()) {
                makeToast("Select date of travel")
            } else if (edt_comment.text.trim().toString().isEmpty()) {
                makeToast("Enter comment")
            } else if (edt_adult.text.trim().toString().isEmpty()) {
                makeToast("Enter adult count")
            } else if (edt_child.text.trim().toString().isEmpty()) {
                makeToast("Enter child count")
            } else if (edt_duration.text.trim().toString().isEmpty()) {
                makeToast("Enter duration")
            } else if (edt_quotation_amt.text.trim().toString().isEmpty()) {
                makeToast("Enter Amount")
            } else if (edt_reminder_date.text.trim().toString().isEmpty()) {
                makeToast("select Reminder date")
            } else if (edt_notes.text.trim().toString().isEmpty()) {
                makeToast("Enter notes")
            } else {
                //api call
                var shopId=args.shop?.id ?: "0"
                viewModel.postEnquiry(
                    AppPreferences.shopInId,
                    edt_destination.text.trim().toString(),
                    edt_date_of_travel.text.trim().toString(),
                    edt_comment.text.trim().toString(),
                    edt_adult.text.trim().toString(),
                    edt_child.text.trim().toString(),
                    edt_duration.text.trim().toString(),
                    edt_quotation_amt.text.trim().toString(),
                    edt_reminder_date.text.trim().toString(),
                    edt_notes.text.trim().toString()
                )
                viewModel.addenqsResponse.observe(viewLifecycleOwner) {
                    if (it != null) {
                        if (it == true) {
                            makeToast("Enquiry added successfully")
                        } else {
                            makeToast("Enquiry added failed")
                        }
                    }

                }
            }


        }

        edt_date_of_travel.setOnClickListener {
            showDatePickerDialog(1, edt_date_of_travel)
        }

        edt_reminder_date.setOnClickListener {
            showDatePickerDialog(2, edt_reminder_date)
        }


        val layoutParams = dialog.window!!.attributes
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.attributes = layoutParams


        tv_close.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    private fun showDatePickerDialog(dateType: Int, edttxt: TextView) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        DatePickerDialog(requireActivity(), { _, year, monthOfYear, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            if (dateType == 1) {
                edttxt.text = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
                selectedDateofTravel =
                    SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
            } else {
                edttxt.text = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
                selectedReminderDate =
                    SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
            }
        }, year, month, day).show()
    }

    fun Fragment.makeToast(text: String, duration: Int = Toast.LENGTH_LONG) {
        activity?.let {
            Toast.makeText(it, text, duration).show()
        }
    }
}