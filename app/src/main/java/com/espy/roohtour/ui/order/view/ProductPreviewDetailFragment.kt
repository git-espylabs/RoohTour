package com.espy.roohtour.ui.order.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.espy.roohtour.R
import com.espy.roohtour.databinding.FragmentProductPreviewDetailBinding
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.products.adapter.SliderAdapter
import com.espy.roohtour.ui.products.models.ImageSlide
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class ProductPreviewDetailFragment:
    BaseFragmentWithBinding<FragmentProductPreviewDetailBinding>(R.layout.fragment_product_preview_detail), View.OnClickListener  {

    private val orderViewModel: OrderViewModel by activityViewModels()
    var slides = listOf<ImageSlide>()
    private var currentPage: Int = 0
    private val handler = Handler(Looper.getMainLooper())
    private var timer: Timer? = null

    private var pdtQuantity = 1

    val args: ProductPreviewDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()

        binding.apply {
            viewParent = this@ProductPreviewDetailFragment
        }

        (activity as OrderActivity).setToolBarTitle(args.pdt?.product_name?:"Product")


        binding.apply {
            desc.text = "Description: \n" + args.pdt?.description
            stock.text = "Stock: 0"
            price.text = "Price: " + requireContext().getString(R.string.amount_rep_float, (args.pdt?.price?:"0.00").toFloat())
            /*mrpTv.text = "MRP: " + requireContext().getString(R.string.amount_rep_float, (args.pdt?.mrp?:"0.00").toFloat())*/
            tvPdtQty.text = pdtQuantity.toString()
        }

        (activity as OrderActivity).showProgress()
        orderViewModel.getProductImagesFromFile(args.pdt?.id?:"0", requireContext())

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAdd->{
                pdtQuantity ++
                binding.tvPdtQty.text = pdtQuantity.toString()
            }
            R.id.btnDelete->{
                if (pdtQuantity > 1) {
                    pdtQuantity --
                    binding.tvPdtQty.text = pdtQuantity.toString()
                }
            }
            R.id.btnAddToCart->{
                if (binding.tvPdtQty.text.toString().toInt() >0) {
                    orderViewModel.addToCart(args.pdt, binding.tvPdtQty.text.toString().toInt())
                    (activity as OrderActivity).onBackPressed()
                } else {
                    showToast("Please enter a valid")
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        orderViewModel._slideImages = MutableLiveData()
        orderViewModel._liveStock = MutableLiveData()
        orderViewModel._slideImagesFromFile = MutableLiveData()
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
        orderViewModel.slideImages.observe(viewLifecycleOwner) {
            orderViewModel.getProductLiveStock(args.pdt?.batchid ?: "0")
            slides = it.ifEmpty {
                val tempList = arrayListOf<ImageSlide>()
                tempList.add(ImageSlide("0"))
                tempList
            }
            setSlider()
        }

        orderViewModel.liveStock.observe(viewLifecycleOwner) {
            (activity as OrderActivity).hideProgress()
            if (it != null && it.isNotEmpty()) {
                binding.stock.text = "Stock: $it"
            } else {
                binding.stock.text = "Stock: 0"
            }
        }

        orderViewModel.slideImagesFromFile.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                orderViewModel.getProductLiveStock(args.pdt?.batchid ?: "0")
                slides = if (it.isNotEmpty()) {
                    it
                } else {
                    val tempList = arrayListOf<ImageSlide>()
                    tempList.add(ImageSlide("0"))
                    tempList
                }
                setSlider()
            } else {
                orderViewModel.getProductsImages(args.pdt?.id ?: "0")
            }
        }
    }
}