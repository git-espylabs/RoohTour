package com.espy.roohtour.ui.order.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.espy.roohtour.R
import com.espy.roohtour.databinding.ActivityOrderBinding
import com.espy.roohtour.ui.base.BaseActivity

class OrderActivity : BaseActivity<ActivityOrderBinding>(
    R.layout.activity_order,
    false,
    R.string.order
){
    private val orderViewModel: OrderViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@OrderActivity
            viewModel = orderViewModel
            viewParent = this@OrderActivity
        }

        when(intent?.hasExtra(ARGS_SHOP_ID)){
            true -> orderViewModel.orderShopId = (intent?.extras?.get(ARGS_SHOP_ID) ?: "0").toString()
        }
    }

    override fun onCreateToolbar(): Toolbar? {
        return binding?.toolbarHolder?.toolbar
    }

    override fun onCreateLoader(): View? {
        return binding?.loadingView?.loaderView
    }

    fun moveToOrderSummary(){
        findNavController(R.id.fragmentContainerView).apply {
            navigate(R.id.orderSummaryFragment, null, getNavOptions())
        }
    }

    private fun getNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_from_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_from_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
    }

    fun setToolBarTitle(title: String){
        binding?.toolbarHolder?.toolbarTitle?.text = title
    }

    companion object{
        const val ARGS_SHOP_ID = "shop_id"
    }
}