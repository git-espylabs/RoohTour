package com.espy.roohtour.ui.products.view

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.espy.roohtour.R
import com.espy.roohtour.api.Result
import com.espy.roohtour.databinding.FragmentTodayMyOrdersBinding
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.home.HomeActivity
import com.espy.roohtour.ui.home.HomeViewModel
import com.espy.roohtour.ui.products.adapter.TodayMyOrderAdapter
import com.espy.roohtour.ui.products.models.TodayMyOrder

class TodayMyOrderFragment:
    BaseFragmentWithBinding<FragmentTodayMyOrdersBinding>(R.layout.fragment_today_my_orders) {

    private val homeViewModel: HomeViewModel by activityViewModels()

    private var productList = listOf<TodayMyOrder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = homeViewModel
        }

        (activity as HomeActivity).setToolBarProperties(getString(R.string.today_my_orders), isHome = false)

        setObservers()

        (activity as HomeActivity).showProgress()
        homeViewModel.getTodayMyOrdersList()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.actionLogout).isVisible = false
        menu.findItem(R.id.actionSearch).isVisible = false
    }

    private fun setObservers(){
        homeViewModel.todayMyOrders.observe(viewLifecycleOwner) {
            (activity as HomeActivity).hideProgress()
            if (it is Result.Success && it.data.any()) {
                loadList(it.data)
            } else {
                showToast("No order today!")
            }
        }
    }

    private fun loadList(list: List<TodayMyOrder>){
        binding.productList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = TodayMyOrderAdapter(requireContext(), list){ catId ->
                (activity as HomeActivity).showProgress()
            }.apply {
                notifyDataSetChanged()
            }
            setHasFixedSize(false)
        }
    }
}