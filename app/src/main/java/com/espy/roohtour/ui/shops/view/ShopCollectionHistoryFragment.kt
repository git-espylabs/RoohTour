package com.espy.roohtour.ui.shops.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.espy.roohtour.R
import com.espy.roohtour.api.Result
import com.espy.roohtour.databinding.FragmentShopCollectionHistoryBinding
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.home.HomeViewModel
import com.espy.roohtour.ui.shops.adapter.ShopPayHistoryAdapter
import com.espy.roohtour.ui.shops.models.ShopPayHistory
import java.text.SimpleDateFormat
import java.util.*

class ShopCollectionHistoryFragment:
    BaseFragmentWithBinding<FragmentShopCollectionHistoryBinding>(R.layout.fragment_shop_collection_history) {

    private val homeViewModel: HomeViewModel by activityViewModels()

    val DATE_FORMAT = "dd MMM yyyy"
    val DATE_FORMAT_SERVER = "yyyy-MM-dd"
    var selectedDateFrom = "";
    var selectedDateTo = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedDateFrom = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(Calendar.getInstance().time)
        selectedDateTo = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(Calendar.getInstance().time)

        binding.apply {
            viewModel = homeViewModel
            filterDateFrom.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Calendar.getInstance().time)
            filterDateTo.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Calendar.getInstance().time)
        }

        (activity as ShopDetailsActivity).setToolBarProperties(getString(R.string.shop_collection_history))

        setObservers()

        (activity as ShopDetailsActivity).showProgress()
        homeViewModel.getShopCollectionHistory(ShopDetailsActivity.currentShop?.id?:"0", selectedDateFrom, selectedDateTo)



        binding.filterDateTo.setOnClickListener {
            showDatePickerDialog(1)
        }

        binding.filterDateFrom.setOnClickListener {
            showDatePickerDialog(2)
        }

        binding.btnShow.setOnClickListener {
            (activity as ShopDetailsActivity).showProgress()
            homeViewModel.getShopCollectionHistory(ShopDetailsActivity.currentShop?.id?:"0", selectedDateFrom, selectedDateTo)
        }
    }

    private fun setObservers(){
        homeViewModel.shopCollectionHistoryList.observe(viewLifecycleOwner) {
            (activity as ShopDetailsActivity).hideProgress()
            if (it is Result.Success && it.data.any()) {
                loadList(it.data)
            } else {
                showToast("No data found!")
                loadList(listOf())
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.actionFeedback).isVisible = false
    }

    private fun loadList(list: List<ShopPayHistory>){
        binding.productList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ShopPayHistoryAdapter(requireContext(), list){ _ ->
                (activity as ShopDetailsActivity).showProgress()
            }.apply {
                notifyDataSetChanged()
            }
            setHasFixedSize(false)
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
                binding.filterDateTo.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(c.time)
                selectedDateTo = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
            } else {
                binding.filterDateFrom.text = SimpleDateFormat(DATE_FORMAT, Locale.US).format(c.time)
                selectedDateFrom = SimpleDateFormat(DATE_FORMAT_SERVER, Locale.US).format(c.time)
            }
        }, year, month, day).show()
    }
}