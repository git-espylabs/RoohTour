package com.espy.roohtour.ui.products.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.espy.roohtour.R
import com.espy.roohtour.api.models.products.Product
import com.espy.roohtour.databinding.ActivityProductDetailsBinding
import com.espy.roohtour.ui.base.BaseActivity

class ProductDetailsActivity : BaseActivity<ActivityProductDetailsBinding>(
    R.layout.activity_product_details,
    true,
    R.string.products
) {
    private lateinit var viewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        binding?.apply {
            lifecycleOwner = this@ProductDetailsActivity
            this.viewModel = viewModel
        }
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        if (intent.hasExtra(PROD_ARGS)){
            viewModel.productId = intent.getParcelableExtra<Product>(PROD_ARGS)?.id.toString()
            selectedProduct = intent.getParcelableExtra<Product>(PROD_ARGS)
            supportActionBar?.setTitle(selectedProduct?.product_name?:"Cap Product")
        }else{
            supportActionBar?.setTitle("Cap Product")
        }

        /*val controller = (supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController

        controller.setGraph(controller.graph, intent.extras)*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateToolbar(): Toolbar? {
        return binding?.toolbarHolder?.toolbar
    }

    override fun onCreateLoader(): View? {
        return binding?.loadingView?.loaderView
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }

    companion object{
        const val PROD_ARGS = "pdt"
        var selectedProduct: Product? = null
    }
}