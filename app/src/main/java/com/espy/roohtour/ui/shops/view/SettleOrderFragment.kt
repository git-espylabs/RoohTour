package com.espy.roohtour.ui.shops.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.espy.roohtour.R
import com.espy.roohtour.api.Result
import com.espy.roohtour.api.models.shops.OrderItem
import com.espy.roohtour.databinding.FragmentSetleOrderBinding
import com.espy.roohtour.extensions.sumBy
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.shops.adapter.OrderItemListAdapter

class SettleOrderFragment:
    BaseFragmentWithBinding<FragmentSetleOrderBinding>(R.layout.fragment_setle_order) {

    private val shopsViewModel: ShopsViewModel by activityViewModels()
    private val args: SettleOrderFragmentArgs by navArgs()
    private var orderItemListAdapter: OrderItemListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = shopsViewModel
//            (activity as OrderHistoryActivity).setToolBarProperties(args.deliveryShop?.shop_name?:getString(R.string.order_history), false)

            shAddress.text = "Address: " + args.deliveryShop?.shop_address?:""
            //shGst.text = "GST Number: " + args.deliveryShop?.shop_regi_no?:"0"
        }

        //binding.invNo.text = "Invoice Number: " + args.deliveryShop?.invoice_id

        setObservers()

        (activity as OrderHistoryActivity).showProgress()
        shopsViewModel.getPendingOrderItems(args.pendingorder?.id?:"0")
    }

    private fun setObservers(){
        shopsViewModel.orderItemsList.observe(viewLifecycleOwner) {
            (activity as OrderHistoryActivity).hideProgress()
            if (it is Result.Success && it.data.any()) {
                loadOrderItems(it.data)
            } else {
                showToast("No products found!")
            }
        }
    }

    private fun loadOrderItems(list: List<OrderItem>){
        orderItemListAdapter = OrderItemListAdapter(requireContext(), list){

        }.apply {
            notifyDataSetChanged()
        }

        binding.rvDetailList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderItemListAdapter
            setHasFixedSize(false)
        }
        updateSummary(list)
    }

    private fun updateSummary(list: List<OrderItem>){
        list.sumBy { item ->
            item?.run {
                total_amount.toFloat()
            }?: run {
                0F
            }
        }.apply {
            binding.also {
                it.grandTotal.text = requireContext().getString(R.string.amount_rep_float, this)
                it.itemTotal.text = list.size.toString()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        shopsViewModel._orderItemsList = MutableLiveData()
    }
}