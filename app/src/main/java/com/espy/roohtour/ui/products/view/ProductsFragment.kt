package com.espy.roohtour.ui.products.view

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.espy.roohtour.R
import com.espy.roohtour.api.Result
import com.espy.roohtour.api.models.products.Category
import com.espy.roohtour.api.models.products.Product
import com.espy.roohtour.app.InstanceManager
import com.espy.roohtour.databinding.FragmentProductsBinding
import com.espy.roohtour.extensions.launchActivity
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.home.HomeActivity
import com.espy.roohtour.ui.home.HomeViewModel
import com.espy.roohtour.ui.products.adapter.CategoryListAdapter
import com.espy.roohtour.ui.products.adapter.ProductListAdapter

class ProductsFragment:
    BaseFragmentWithBinding<FragmentProductsBinding>(R.layout.fragment_products) {

    private val homeViewModel: HomeViewModel by activityViewModels()

    private var productList = listOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = homeViewModel
        }

        (activity as HomeActivity).setToolBarProperties(getString(R.string.products), isHome = false)

        setObservers()

        with(homeViewModel){
            if (myCategories.isEmpty()){
                (activity as HomeActivity).showProgress()
                getCategoriesList()
            }else{
                loadCategories(myCategories)
                (activity as HomeActivity).showProgress()
                homeViewModel.getProductsFromDbByCategory(myCategories[0].id?:"0")
            }
        }

        binding.iconsearch.setOnClickListener {
            binding.etSearch.setText("")
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.actionLogout).isVisible = false
        menu.findItem(R.id.actionSearch).isVisible = false
    }

    private fun setObservers(){
        homeViewModel.productsByCategory.observe(viewLifecycleOwner) {
            (activity as HomeActivity).hideProgress()
            if (it is Result.Success && it.data.any()) {
                productList = it.data
                loadProducts(it.data)
                setAutoCompleteSearch()
            } else {
                showToast("No products found")
                productList = listOf()
                loadProducts(listOf())
                setAutoCompleteSearch()
            }
        }
        homeViewModel.categoriesList.observe(viewLifecycleOwner) {
            (activity as HomeActivity).hideProgress()
            if (it is Result.Success && it.data.any()) {
                loadCategories(it.data)
                (activity as HomeActivity).showProgress()
                homeViewModel.getProductsFromDbByCategory(it.data[0].id ?: "0")
            } else {
                showToast("No categories found")
            }
        }
    }

    private fun loadProducts(list: List<Product>){
        binding.productList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ProductListAdapter(requireContext(), list){ product ->
                product?.run {
                    activity?.launchActivity<ProductDetailsActivity> {
                        this.putExtra(ProductDetailsActivity.PROD_ARGS, this@run)
                    }
                }
            }.apply {
                notifyDataSetChanged()
            }
            setHasFixedSize(false)
        }
    }

    private fun loadCategories(list: List<Category>){
        homeViewModel.myCategories = list
        InstanceManager.categoriesList = list
        binding.categoryList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoryListAdapter(requireContext(), list){ catId ->
                (activity as HomeActivity).showProgress()
                homeViewModel.getProductsFromDbByCategory(catId?:"0")
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
                    (activity as HomeActivity).hideKeyboard()
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