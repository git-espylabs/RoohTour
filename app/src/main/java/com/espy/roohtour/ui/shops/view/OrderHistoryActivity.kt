package com.espy.roohtour.ui.shops.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.espy.roohtour.R
import com.espy.roohtour.databinding.ActivityOrderHostoryBinding
import com.espy.roohtour.ui.base.BaseActivity

class OrderHistoryActivity : BaseActivity<ActivityOrderHostoryBinding>(
    R.layout.activity_order_hostory,
    true,
    R.string.order_history
), NavController.OnDestinationChangedListener{

    private lateinit var viewModel: ShopsViewModel
    private lateinit var controller: NavController
    private lateinit var navHostFragment: NavHostFragment

    private var currentDestination = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ShopsViewModel::class.java)
        binding?.apply {
            lifecycleOwner = this@OrderHistoryActivity
            this.viewModel = viewModel
        }

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        controller = navHostFragment.navController
    }

    override fun onCreateToolbar(): Toolbar? {
        return binding?.toolbarHolder?.toolbar
    }

    override fun onCreateLoader(): View? {
        return binding?.loadingView?.loaderView
    }

    fun setToolBarProperties(title: String, isHome: Boolean){
        binding?.toolbarHolder?.toolbarTitle?.text = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        controller.addOnDestinationChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        controller.removeOnDestinationChangedListener(this)
    }

    private fun getNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_from_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_from_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

    }


}