package com.espy.roohtour.ui.order.view

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.espy.roohtour.R
import com.espy.roohtour.api.Result
import com.espy.roohtour.api.models.products.Category
import com.espy.roohtour.api.models.products.Product
import com.espy.roohtour.databinding.FragmentProductsPreviewBinding
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.order.adapter.ProductPreviewListAdapter
import com.espy.roohtour.ui.products.adapter.CategoryListAdapter

class ProductsPreviewFragment:
    BaseFragmentWithBinding<FragmentProductsPreviewBinding>(R.layout.fragment_products_preview), View.OnClickListener{

    private val orderViewModel: OrderViewModel by activityViewModels()

    private var productList = listOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = orderViewModel
            viewParent = this@ProductsPreviewFragment
        }


        (activity as OrderActivity).setToolBarTitle("Create Order")

        setObservers()

        with(orderViewModel){
            if (myCategories.isEmpty()){
                (activity as OrderActivity).showProgress()
                getCategoriesList()
            }else{
                loadCategories(myCategories)
                (activity as OrderActivity).showProgress()
                getProductsFromDbByCategory(myCategories[0].id?:"0")
            }

            getCartProductsCount()
        }

        binding.iconsearch.setOnClickListener {
            binding.etSearch.setText("")
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cartFab ->{
                findNavController().navigate(ProductsPreviewFragmentDirections.actionProductPreviewFragmentToOrderSummaryFragment())
            }
        }
    }

    private fun setObservers(){
        orderViewModel.productsByCategory.observe(viewLifecycleOwner, {
            (activity as OrderActivity).hideProgress()
            if (it is Result.Success && it.data.any()){
                productList = it.data
                loadProducts(it.data)
                setAutoCompleteSearch()
            }else{
                showToast("No products found")
                productList = listOf()
                loadProducts(listOf())
                setAutoCompleteSearch()
            }
        })

        orderViewModel.categoriesList.observe(viewLifecycleOwner, {
            (activity as OrderActivity).hideProgress()
            if (it is Result.Success && it.data.any()){
                loadCategories(it.data)
                (activity as OrderActivity).showProgress()
                orderViewModel.getProductsFromDbByCategory(it.data[0].id?:"0")
            }else{
                showToast("No categories found")
            }
        })

        orderViewModel.cartCount.observe(viewLifecycleOwner, {
            if (it > 0){
                binding.cartFab.count = it
            }
        })
    }

    private fun loadCategories(list: List<Category>){
        orderViewModel.myCategories = list
        binding.categoryList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoryListAdapter(requireContext(), list){ catId ->
                (activity as OrderActivity).showProgress()
                orderViewModel.getProductsFromDbByCategory(catId?:"0")
            }.apply {
                notifyDataSetChanged()
            }
            setHasFixedSize(false)
        }
    }

    private fun loadProducts(list: List<Product>){
        binding.productList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ProductPreviewListAdapter(requireContext(), list){ product ->
                product?.run {
                    findNavController().navigate(ProductsPreviewFragmentDirections.actionProductPreviewFragmentToProductPreviewDetailFragment(this))
                }
            }.apply {
                notifyDataSetChanged()
            }
            setHasFixedSize(false)
        }
    }

    private fun setAutoCompleteSearch(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, productList)
        binding.etSearch.apply {
            threshold = 1
            setTextColor(Color.BLACK)
            setAdapter(adapter)

            setOnItemClickListener { parent, _, position, _ ->

                productList.find {
                    it.id == (parent.getItemAtPosition(position) as Product).id
                }?.run {
                    val tempList = arrayListOf<Product>()
                    tempList.add(this)
                    loadProducts(tempList.toList())
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
                        loadProducts(productList)
                    }
                }
            })
        }
    }
}