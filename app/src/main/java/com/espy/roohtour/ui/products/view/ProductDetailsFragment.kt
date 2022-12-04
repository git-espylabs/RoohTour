package com.espy.roohtour.ui.products.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.espy.roohtour.R
import com.espy.roohtour.databinding.FragmentProductDetailsBinding
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.products.adapter.SliderAdapter
import com.espy.roohtour.ui.products.models.ImageSlide
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class ProductDetailsFragment:
    BaseFragmentWithBinding<FragmentProductDetailsBinding>(R.layout.fragment_product_details)  {

    private val productsViewModel: ProductsViewModel by activityViewModels()
    var slides = listOf<ImageSlide>()
    private var currentPage: Int = 0
    private val handler = Handler(Looper.getMainLooper())
    private var timer: Timer? = null

//    private val args : ProductDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()

        binding.apply {
            desc.text = "Description: \n" + ProductDetailsActivity.selectedProduct?.description
            stock.text = "Stock: "
            price.text = "Price: " + requireContext().getString(R.string.amount_rep_float, (ProductDetailsActivity.selectedProduct?.price?:"0.00").toFloat())
        }

        (activity as ProductDetailsActivity).showProgress()
        productsViewModel.getProductImagesFromFile(ProductDetailsActivity.selectedProduct?.id?:"0", requireContext())

    }

    override fun onPause() {
        super.onPause()
        productsViewModel._slideImages = MutableLiveData()
        productsViewModel._liveStock = MutableLiveData()
        productsViewModel._slideImagesFromFile = MutableLiveData()
    }

    private fun startAutoSwipe() {
        val update = Runnable {
            if (currentPage == slides.size) {
                currentPage = 0
            }

            //The second parameter ensures smooth scrolling
            binding.sliderPager.setCurrentItem(currentPage++, true)
        }

        timer = timer ?: Timer()
        timer?.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(update)
            }
        }, 3500L, 3500L)
    }

    private fun setSlider(){

        binding.apply {

            val slidingCallback = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentPage = position
                }
            }

            sliderPager.run {
                adapter = SliderAdapter(context, slides)
                registerOnPageChangeCallback(slidingCallback)
                startAutoSwipe()
            }

            TabLayoutMediator(tablay, sliderPager) { _, _ ->

            }.attach()
        }
    }

    private fun setObserver(){
        productsViewModel.slideImages.observe(viewLifecycleOwner, {
            productsViewModel.getProductLiveStock(ProductDetailsActivity.selectedProduct?.batchid?:"0")
            slides = if (it.isNotEmpty()) {
                it
            } else {
                val tempList = arrayListOf<ImageSlide>()
                tempList.add(ImageSlide("0"))
                tempList
            }
            setSlider()
        })

        productsViewModel.liveStock.observe(viewLifecycleOwner, {
            (activity as ProductDetailsActivity).hideProgress()
            if (it != null && it.isNotEmpty()){
                binding.stock.text = "Stock: $it"
            }else{
                binding.stock.text = "Stock: 0"
            }
        })

        productsViewModel.slideImagesFromFile.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                productsViewModel.getProductLiveStock(ProductDetailsActivity.selectedProduct?.batchid?:"0")
                slides = if (it.isNotEmpty()) {
                    it
                } else {
                    val tempList = arrayListOf<ImageSlide>()
                    tempList.add(ImageSlide("0"))
                    tempList
                }
                setSlider()
            } else {
                productsViewModel.getProductsImages(ProductDetailsActivity.selectedProduct?.id?:"0")
            }
        })
    }
}