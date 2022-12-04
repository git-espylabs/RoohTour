package com.espy.roohtour.ui.shops.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.espy.roohtour.R
import com.espy.roohtour.databinding.ActivityAddNewShopActivtyBinding
import com.espy.roohtour.ui.base.BaseActivity

class AddNewShopActivty : BaseActivity<ActivityAddNewShopActivtyBinding>(
    R.layout.activity_add_new_shop_activty,
    true,
    R.string.add_shop
){

    private lateinit var viewModel: ShopsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ShopsViewModel::class.java)
        binding?.apply {
            lifecycleOwner = this@AddNewShopActivty
            this.viewModel = viewModel
        }

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}