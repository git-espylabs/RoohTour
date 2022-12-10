package com.espy.roohtour.ui.shops.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.espy.roohtour.R
import com.espy.roohtour.api.Result
import com.espy.roohtour.databinding.FragmentSetleOrderBinding
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.shops.adapter.OrderItemListAdapter
import com.espy.roohtour.utils.CommonUtils
import java.text.SimpleDateFormat
import java.util.*


class SettleOrderFragment:
    BaseFragmentWithBinding<FragmentSetleOrderBinding>(R.layout.fragment_setle_order) {

    private val shopsViewModel: ShopsViewModel by activityViewModels()
    private val args: SettleOrderFragmentArgs by navArgs()
    val DATE_FORMAT = "dd MMM yyyy"
    val DATE_FORMAT_SERVER = "yyyy-MM-dd"

    var selectedDateFollowUp = "";
    var selectedDateAmmend = "";
    var confirmationStatus = "0"
    var notes = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as OrderHistoryActivity).setToolBarProperties("Add Follow Up", false)
        selectedDateFollowUp = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(Calendar.getInstance().time)
        selectedDateAmmend = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(Calendar.getInstance().time)

        binding.apply {
            viewModel = shopsViewModel

            dot.text = "Date Of Travel: " + CommonUtils.getConvertedDate2(args.pendingorder?.date_of_travel?:"") + ", "
            destiny.text = "Destination: " + args.pendingorder?.destination
            tvComt.text = "Comment: " + args.pendingorder?.comment
            tvAdultCount.text = "Adults: " + args.pendingorder?.adult + ", "
            tvAChildCount.text = "Children: " + args.pendingorder?.child + ", "
            tvDuration.text = "Duration: " + args.pendingorder?.duration
            tvQuotation.text = "Quotation: " + args.pendingorder?.quotation_amount + ", "
            tvReminder.text = "Reminder On: " + CommonUtils.getConvertedDate2(args.pendingorder?.reminder_date?:"")
            tvNotes.text = "Notes: " + args.pendingorder?.notes
            tvReceiveDate.text = "Receive On: " + CommonUtils.getConvertedDate2(args.pendingorder?.recieve_date?:"")

            followupDate.also {
                it.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Calendar.getInstance().time)
                it.setOnClickListener {
                    showDatePickerDialog(1)
                }
            }
            ammendDate.also {
                it.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Calendar.getInstance().time)
                it.setOnClickListener {
                    showDatePickerDialog(2)
                }
            }


            rgConfirmStatus.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rbStatusWhite -> confirmationStatus = "0"
                    R.id.rbStatusGreen -> confirmationStatus = "1"
                    R.id.rbStatusBlue -> confirmationStatus = "2"
                    R.id.rbStatusYellow -> confirmationStatus = "3"
                    R.id.rbStatusRed -> confirmationStatus = "4"
                }
            }

            btnFollowup.setOnClickListener {
                notes = etNotes.text.toString()
                (activity as OrderHistoryActivity).showProgress()
                shopsViewModel.followUpEnquiry(
                    args.deliveryShop?.agency_id?:"0",
                    args.pendingorder?.enquiry_id?:"0",
                    selectedDateFollowUp,
                    confirmationStatus,
                    selectedDateAmmend,
                    notes
                )
            }
        }

        setObservers()
    }

    private fun setObservers(){
        shopsViewModel.followupRes.observe(viewLifecycleOwner) {
            (activity as OrderHistoryActivity).hideProgress()
            if (it is Result.Success && it.data == 1) {
                showToast("Follow Up Success!")
                (requireActivity() as OrderHistoryActivity).onBackPressed()
            } else {
                showToast("Follow Up failed! Contact Admin")
            }
        }
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
                binding.followupDate.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(c.time)
                selectedDateFollowUp = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
            } else {
                binding.ammendDate.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(c.time)
                selectedDateAmmend = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
            }
        }, year, month, day).show()
    }

    override fun onPause() {
        super.onPause()
        shopsViewModel._followupRes = MutableLiveData()
    }
}