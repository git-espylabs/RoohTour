package com.espy.roohtour.ui.home
import com.espy.roohtour.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.espy.roohtour.app.AppSettings
import com.espy.roohtour.app.InstanceManager
import com.espy.roohtour.databinding.ActivityHomeBinding
import com.espy.roohtour.extensions.launchActivity
import com.espy.roohtour.extensions.setImageTint
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.ui.base.BaseActivity
import com.espy.roohtour.ui.profile.view.LoginActivity
import com.espy.roohtour.ui.profile.view.ProfileActivity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.espy.roohtour.ui.shops.view.OrderHistoryActivity


class HomeActivity : BaseActivity<ActivityHomeBinding>(
    R.layout.activity_home,
    false,
    R.string.app_name
), View.OnClickListener, NavController.OnDestinationChangedListener {

    private var doubleBackToExitPressedOnce = false
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var controller: NavController
    private lateinit var navHostFragment: NavHostFragment
    private val EXIT_DELAY = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@HomeActivity
            this.viewModel = viewModel
            viewParent = this@HomeActivity
        }
        navHostFragment = supportFragmentManager
                .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        controller = navHostFragment.navController

        binding?.toolbarHolder?.ivProfile?.setOnClickListener(this)
        binding?.toolbarHolder?.toolbarTitle?.setOnClickListener(this)

        setObserver()
    }

    override fun onCreateToolbar(): Toolbar? {
        return binding?.toolbarHolder?.toolbar
    }

    override fun onCreateLoader(): View? {
        return binding?.loadingView?.loaderView
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        (menuInflater).apply {
            inflate(R.menu.menu_home, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionLogout){
            AppPreferences.userId = ""
            launchActivity<LoginActivity>()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when(destination.id){
            R.id.homeFragment ->{
                binding?.apply {
                    btnHome.setImageTint(R.color.app_accent_color)
                    btnProduct.setImageTint(R.color.grey_100)
                    btnShop.setImageTint(R.color.grey_100)
                    btnMyOrders.setImageTint(R.color.grey_100)
                }
            }
            R.id.productsFragment ->{
                binding?.apply {
                    btnHome.setImageTint(R.color.grey_100)
                    btnProduct.setImageTint(R.color.app_accent_color)
                    btnShop.setImageTint(R.color.grey_100)
                    btnMyOrders.setImageTint(R.color.grey_100)
                }
            }
            R.id.shopFragment ->{
                binding?.apply {
                    btnHome.setImageTint(R.color.grey_100)
                    btnProduct.setImageTint(R.color.grey_100)
                    btnShop.setImageTint(R.color.app_accent_color)
                    btnMyOrders.setImageTint(R.color.grey_100)
                }
            }
            R.id.todayMyOrderFragment ->{
                binding?.apply {
                    btnHome.setImageTint(R.color.grey_100)
                    btnProduct.setImageTint(R.color.grey_100)
                    btnShop.setImageTint(R.color.grey_100)
                    btnMyOrders.setImageTint(R.color.app_accent_color)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        controller.addOnDestinationChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        controller.removeOnDestinationChangedListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.l1 ->{
                findNavController(R.id.fragmentContainerView).apply {
                    navigate(R.id.homeFragment, null, getNavOptions())
                }
            }
            R.id.l2 ->{
                findNavController(R.id.fragmentContainerView).apply {
                    navigate(R.id.productsFragment, null, getNavOptions())
                }
            }
            R.id.l3 ->{
                viewModel._isShoppedIn = MutableLiveData()
                viewModel._isShoppedOut = MutableLiveData()
                findNavController(R.id.fragmentContainerView).apply {
                    navigate(R.id.shopFragment, null, getNavOptions())
                }
            }
            R.id.l4 ->{
                /*findNavController(R.id.fragmentContainerView).apply {
                    navigate(R.id.todayMyOrderFragment, null, getNavOptions())
                }*/

                launchActivity<OrderHistoryActivity>()
            }
            R.id.ivProfile ->{
                launchActivity<ProfileActivity>()
            }
            R.id.toolbarTitle ->{
                launchActivity<ProfileActivity>()
            }
        }
    }

    override fun onBackPressed() {

        when {
            controller.currentDestination?.id == R.id.homeFragment -> {

                if (doubleBackToExitPressedOnce) {
                    val setIntent = Intent(Intent.ACTION_MAIN)
                    setIntent.addCategory(Intent.CATEGORY_HOME)
                    setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(setIntent)
                    finish()
                }

                this.doubleBackToExitPressedOnce = true
                showToast(getString(R.string.exit_tap_alert))

                Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, EXIT_DELAY)
            }
            supportFragmentManager.backStackEntryCount > 0 -> {
                super.onBackPressed()
            }
            else -> {
                findNavController(R.id.fragmentContainerView).apply {
                    navigate(R.id.homeFragment, null, getNavOptions())
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

    fun setToolBarProperties(title: String, isHome: Boolean){
        binding?.toolbarHolder?.toolbarTitle?.text = title
        if (isHome){
            binding?.toolbarHolder?.ivProfile?.visibility = View.VISIBLE
        }else{
            binding?.toolbarHolder?.ivProfile?.visibility = View.GONE
        }
    }

    private fun setObserver(){
        viewModel.myProfile.observe(this, {
            InstanceManager.profile = it
            binding?.toolbarHolder?.toolbarTitle?.text = InstanceManager.profile?.staff_name?:""
            binding?.toolbarHolder?.ivProfile?.let { it1 ->
                Glide
                    .with(this)
                    .load(AppSettings.endPoints.IMAGE_ASSETS + InstanceManager.profile?.image)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(it1)
            }
        })

        viewModel.expenseSubmitStatus.observe(this, {
            if (it){
                showToast("Expense added successfully!")
            }else{
                showToast(getString(R.string.something_went_wrong))
            }
        })
    }


}