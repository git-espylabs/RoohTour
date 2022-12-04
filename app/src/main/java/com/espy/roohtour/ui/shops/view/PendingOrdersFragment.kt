package com.espy.roohtour.ui.shops.view

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.espy.roohtour.R
import com.espy.roohtour.api.Result
import com.espy.roohtour.api.models.shops.EnquiryItem
import com.espy.roohtour.api.models.shops.PendingOrder
import com.espy.roohtour.databinding.FragmentPendingOrdersBinding
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.shops.adapter.PendingOrderListAdapter

class PendingOrdersFragment:
    BaseFragmentWithBinding<FragmentPendingOrdersBinding>(R.layout.fragment_pending_orders) {

    private val shopsViewModel: ShopsViewModel by activityViewModels()
    private var pendingOrderListAdapter: PendingOrderListAdapter? = null
    private val args: PendingOrdersFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = shopsViewModel

            (activity as OrderHistoryActivity).setToolBarProperties(args.deliveryShop?.shop_name?:getString(R.string.order_history), false)

            shAddress.text = "Address: " + args.deliveryShop?.shop_address?:""
            //shGst.text = "GST Number: " + args.deliveryShop?.shop_regi_no?:"0"

            icSrch.setOnClickListener {
                binding.srch.setText("")
            }
        }

        setObservers()
        (activity as OrderHistoryActivity).showProgress()
        shopsViewModel.getEnquiryOfAgencyListAsync(args.deliveryShop?.agency_id?:"0")
    }

    override fun onStop() {
        super.onStop()
        shopsViewModel._pendingOrderList = MutableLiveData()
    }

    private fun setObservers(){
        shopsViewModel.enquiry_List.observe(viewLifecycleOwner) {
            (activity as OrderHistoryActivity).hideProgress()
            if (it is Result.Success && it.data.any()) {
//                setAutoCompleteSearch(it.data)
                loadOrders(it.data)
            } else {
                showToast("No Enquiries found!")
            }
        }
    }

    private fun loadOrders(list: List<EnquiryItem>){
        pendingOrderListAdapter = PendingOrderListAdapter(requireContext(), list){ item ->
            val action = item.let {
                PendingOrdersFragmentDirections.actionPendingOrderFragmentToSettleOrderFragment(it, args.deliveryShop)
            }
            action.let { findNavController().navigate(it) }
        }.apply {
            notifyDataSetChanged()
        }

        binding.rvOrderList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pendingOrderListAdapter
            setHasFixedSize(false)
        }
    }

    private fun setAutoCompleteSearch(list: List<PendingOrder>){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list)
        binding.srch.apply {
            threshold = 1
            setTextColor(Color.BLACK)
            setAdapter(adapter)

            setOnItemClickListener { parent, _, position, _ ->

                list.find {
                    it.id == (parent.getItemAtPosition(position) as PendingOrder).id
                }?.run {

                    /*val action = this?.let {
                        PendingOrdersFragmentDirections.actionPendingOrderFragmentToSettleOrderFragment(it)
                    }
                    action?.let { findNavController().navigate(it) }*/
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
                    }
                }
            })
        }
    }


}