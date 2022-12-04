package com.espy.roohtour.ui.shops.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.espy.roohtour.R
import com.espy.roohtour.databinding.ActivityShopDetailsBinding
import com.espy.roohtour.api.models.shops.Shop
import com.espy.roohtour.ui.base.BaseActivity

class ShopDetailsActivity : BaseActivity<ActivityShopDetailsBinding>(
    R.layout.activity_shop_details,
    true,
    R.string.shop_details
), NavController.OnDestinationChangedListener {

    private lateinit var viewModel: ShopsViewModel
    private lateinit var controller: NavController
    private lateinit var navHostFragment: NavHostFragment




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ShopsViewModel::class.java)
        binding?.apply {
            lifecycleOwner = this@ShopDetailsActivity
            this.viewModel = viewModel
        }
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        controller = navHostFragment.navController

        if (intent.hasExtra(SHOP_ARGS)){
            viewModel.shopId = intent.getParcelableExtra<Shop>(SHOP_ARGS)?.id.toString()
            viewModel.shopLoginId = intent.getParcelableExtra<Shop>(SHOP_ARGS)?.login_id.toString()
        }

        val controller = (supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController

        controller.setGraph(controller.graph, intent.extras)

        setObservers()
    }

    override fun onResume() {
        super.onResume()
        controller.addOnDestinationChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        controller.removeOnDestinationChangedListener(this)
    }

    override fun onCreateToolbar(): Toolbar? {
        return binding?.toolbarHolder?.toolbar
    }

    override fun onCreateLoader(): View? {
        return binding?.loadingView?.loaderView
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        (menuInflater).apply {
            inflate(R.menu.menu_shop_details_screen, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionFeedback){
            ShopFeedbackBottomSheetDialogFragment(viewModel).show(
                supportFragmentManager,
                "ShopFeedbackFragment"
            )

        }else if (item.itemId == android.R.id.home) {
                onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when{
            controller.currentDestination?.id == R.id.shopDetailsFragment -> {
                finish()
            }
            supportFragmentManager.backStackEntryCount > 0 -> {
                super.onBackPressed()
            }
            else -> {
                findNavController(R.id.fragmentContainerView).apply {
                    navigate(R.id.shopDetailsFragment, null, getNavOptions())
                }
            }

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

    private fun setObservers(){
        viewModel.feedbackResponse.observe(this) {
            if (it) {
                showToast(getString(R.string.feedback_submitted))
            } else {
                showToast(getString(R.string.something_went_wrong))
            }
        }
    }

    fun setToolBarProperties(title: String){
        binding?.toolbarHolder?.toolbarTitle?.text = title
    }

    companion object{
        const val SHOP_ARGS = "shop"
        var currentShop: Shop? = null
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

    }

}