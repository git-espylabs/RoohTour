package com.espy.roohtour.ui.order.view

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.espy.roohtour.R
import com.espy.roohtour.databinding.FragmentOrderSummaryBinding
import com.espy.roohtour.extensions.sumBy
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.order.adapter.OrderSummaryAdapter
import kotlinx.coroutines.DelicateCoroutinesApi

class OrderSummaryFragment:
    BaseFragmentWithBinding<FragmentOrderSummaryBinding>(R.layout.fragment_order_summary) {

    private val orderViewModel: OrderViewModel by activityViewModels()

    private lateinit var orderSummaryAdapter: OrderSummaryAdapter

    var discountAmount = 0.0

    var lastTappedTime = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = orderViewModel
            viewParent = this@OrderSummaryFragment
        }

        binding.btnContinue.setOnClickListener {
            if ((SystemClock.elapsedRealtime() - lastTappedTime) < 1000){
                return@setOnClickListener
            }
            lastTappedTime = SystemClock.elapsedRealtime()
            if (orderViewModel.orderProductsList.isNotEmpty()){
                showUploadWarning()
            }
        }

        discountAmount = AppPreferences.orderDiscount
        binding.etDiscount.setText(discountAmount.toString())
        if (discountAmount > 0.0){
            binding.btnApplyDiscount.text = "Remove"
        }else{
            binding.btnApplyDiscount.text = "Apply"
        }

        binding.btnApplyDiscount.setOnClickListener {
            if (discountAmount <= 0.0 && binding.etDiscount.text.toString().trim().isNotEmpty()){
                discountAmount = binding.etDiscount.text.toString().trim().toDouble()
                AppPreferences.orderDiscount = discountAmount
                updateOrderSummaryValue()
                binding.btnApplyDiscount.text = "Remove"
            }else if (discountAmount > 0){
                discountAmount = 0.0
                AppPreferences.orderDiscount = 0.0
                binding.etDiscount.setText("")
                updateOrderSummaryValue()
                binding.btnApplyDiscount.text = "Apply"
            }else{
                showToast("Please enter a valid amount for discount")
            }
        }

        initRecyclerView()

        setObserver()

        orderViewModel.getAllCartItems()
    }

    override fun onResume() {
        super.onResume()
        (activity as OrderActivity).setToolBarTitle(getString(R.string.order_summary))
    }

    @DelicateCoroutinesApi
    private fun setObserver(){
        orderViewModel.orderUpload.observe(viewLifecycleOwner, {
            (activity as OrderActivity).hideProgress()
            if (it){
                orderViewModel.emptyCart()
                showUploadResponse(getString(R.string.order_uploaded))
                AppPreferences.orderDiscount = 0.0
            }else{
                binding.apply {
                    btnContinue.text = "Place Order Now"
                    btnContinue.isEnabled = true
                }
                showToast(R.string.order_failed)
            }
        })

        orderViewModel.cartItems.observe(viewLifecycleOwner, {
            orderViewModel.orderProductsList = ArrayList(it)

            orderSummaryAdapter.updateList(orderViewModel.orderProductsList)
            updateOrderSummaryValue()
        })
    }

    private fun initRecyclerView(){
        orderSummaryAdapter = OrderSummaryAdapter(requireContext(), orderViewModel.orderProductsList){ product, isDelete, pos ->
            if (isDelete){
                val qty = orderViewModel.orderProductsList[pos]?.qty
                if ((qty?:"0").toInt() > 1){
                    val updateQty = (qty?:"0").toInt() - 1
                    orderViewModel.updateQuantity(orderViewModel.orderProductsList[pos]?.id?:"0", updateQty.toString())
                }else{
                    orderViewModel.removeProduct(orderViewModel.orderProductsList[pos]?.id?:"0")
                }
            }else{
                val qty = orderViewModel.orderProductsList[pos]?.qty
                val updateQty = (qty?:"0").toInt() + 1
                orderViewModel.updateQuantity(orderViewModel.orderProductsList[pos]?.id?:"0", updateQty.toString())
            }
            orderSummaryAdapter.notifyDataSetChanged()
            updateOrderSummaryValue()
        }

        binding.orderSummaryList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderSummaryAdapter
            setHasFixedSize(false)
        }

        updateOrderSummaryValue()
    }

    private fun updateOrderSummaryValue(){
        orderViewModel.orderProductsList.sumBy { pdt->
            pdt?.run {
                (qty.toInt() * product_price.toFloat())
            }?: run {
              0F
            }
        }.apply {
            val sumTotal = this - discountAmount
            binding.also {
                it.grandTotal.text = requireContext().getString(R.string.amount_rep_float, sumTotal)
                it.itemTotal.text = orderViewModel.orderProductsList.size.toString()
            }
        }
    }

    private fun showUploadWarning() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.confirm_place_order)
            .setPositiveButton("Yes") { dialog, _ ->
                (activity as OrderActivity).showProgress()
                orderViewModel.uploadOrder(discountAmount)
                binding.apply {
                    btnContinue.text = "Processing..."
                    btnContinue.isEnabled = false
                }
                dialog.dismiss()
            }
            .setNegativeButton(
                "Cancel"
            ) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showUploadResponse(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
                activity?.finish()
            }
            .show()
    }
}