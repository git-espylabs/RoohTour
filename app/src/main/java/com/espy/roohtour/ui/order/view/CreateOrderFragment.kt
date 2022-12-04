package com.espy.roohtour.ui.order.view

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.espy.roohtour.R
import com.espy.roohtour.app.InstanceManager
import com.espy.roohtour.databinding.FragmentCreateOrderBinding
import com.espy.roohtour.domain.toOrderProduct
import com.espy.roohtour.api.models.products.Product
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.order.adapter.OrderProductsAdapter


class CreateOrderFragment:
    BaseFragmentWithBinding<FragmentCreateOrderBinding>(R.layout.fragment_create_order), View.OnClickListener {

    private val orderViewModel: OrderViewModel by activityViewModels()

    private lateinit var orderProductsAdapter: OrderProductsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = orderViewModel
            viewParent = this@CreateOrderFragment
        }

        setObservers()
        orderViewModel.getAllProducts()
    }

    override fun onResume() {
        super.onResume()
        (activity as OrderActivity).setToolBarTitle(getString(R.string.order))
    }

    private fun setAutoCompleteSearch(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, InstanceManager.productList)
        binding.etSearch.apply {
            threshold = 1
            setTextColor(Color.BLACK)
            setAdapter(adapter)

            setOnItemClickListener { parent, _, position, _ ->

                InstanceManager.productList.find {
                    it.id == (parent.getItemAtPosition(position) as Product).id
                }?.run {
                    if (orderViewModel.orderProductsList.filter { it?.id == this.id }.any().not()){
                        orderViewModel.orderProductsList.add(toOrderProduct(1))
                        orderProductsAdapter.notifyItemInserted(orderViewModel.orderProductsList.size - 1)
                        setText("")
                    }
                    (activity as OrderActivity).hideKeyboard()
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
                        binding.iconsearch.setImageResource(R.drawable.ic_close_small)
                    }else{
                        binding.iconsearch.setImageResource(R.drawable.ic_search)
                    }
                }
            })
        }
    }

    private fun initRecyclerView(){
        orderProductsAdapter = OrderProductsAdapter(requireContext(), orderViewModel.orderProductsList){ product, isDelete, pos ->
            if (isDelete){
                orderViewModel.orderProductsList[pos]?.apply {
                    qty = (qty.toInt() - 1).toString()
                }
                orderViewModel.orderProductsList.removeAll {
                    it?.id == product?.id && it?.qty?.toInt()?:0 < 1
                }.let {
                    orderProductsAdapter.notifyItemRemoved(pos)
                    orderProductsAdapter.notifyItemRangeChanged(pos, orderViewModel.orderProductsList.size);
                }
            }else{
                orderViewModel.orderProductsList[pos]?.apply {
                    qty = (qty.toInt() + 1).toString()
                }
            }
            orderProductsAdapter.notifyDataSetChanged()
        }
        binding.orderProductList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderProductsAdapter
            setHasFixedSize(false)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iconsearch ->{
                binding.etSearch.setText("")
            }
            R.id.btnContinue ->{
                (activity as OrderActivity).moveToOrderSummary()
            }
        }
    }

    private fun setObservers(){
        orderViewModel.allProductsList.observe(viewLifecycleOwner, {
            if (it.isEmpty()){
                showToast("Please download products to continue!")
            }
            InstanceManager.productList = it
            setAutoCompleteSearch()
            initRecyclerView()
        })
    }
}