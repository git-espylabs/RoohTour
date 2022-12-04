package com.espy.roohtour.ui.sync.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.espy.roohtour.R
import com.espy.roohtour.api.Result
import com.espy.roohtour.app.InstanceManager
import com.espy.roohtour.databinding.FragmentSyncBinding
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.ui.base.BaseFragmentWithBinding

class SyncFragment:
    BaseFragmentWithBinding<FragmentSyncBinding>(R.layout.fragment_sync), View.OnClickListener {

    private lateinit var syncViewModel: SyncViewModel
    var requestPermissionForSync = false


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnSync1 ->{

            }
            R.id.btnSync2 ->{
                (activity as SyncActivity).showProgress()
                syncViewModel.getShopsList()
            }
            R.id.btnSync3 ->{
                (activity as SyncActivity).showProgress()
                syncViewModel.getProductsList(AppPreferences.userId)
            }
            R.id.btnSync4 ->{
                (activity as SyncActivity).showProgress()
                syncViewModel.syncProductImages()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        syncViewModel = ViewModelProvider(this).get(SyncViewModel::class.java)

        binding.apply {
            viewParent = this@SyncFragment
        }

        setObservers()

        syncViewModel.getProductsCount()
        syncViewModel.getShopsCount()
        syncViewModel.getProductImagesCount(requireContext())
    }

    private fun setObservers(){
        syncViewModel.productsList.observe(viewLifecycleOwner) {
            (activity as SyncActivity).hideProgress()
            if (it is Result.Success && it.data.any()) {
                InstanceManager.productList = it.data
                showToast(R.string.sync_complete_products)
            } else {
                showToast(R.string.sync_complete_no_products)
            }
        }
        syncViewModel.shopsList.observe(viewLifecycleOwner) {
            (activity as SyncActivity).hideProgress()
            if (it is Result.Success && it.data.any()) {
                InstanceManager.shopsList = it.data
                showToast(R.string.sync_complete_shops)
            } else {
                showToast(R.string.sync_complete_no_shops)
            }
        }

        syncViewModel.shopsCount.observe(viewLifecycleOwner) {
            binding.shpCount.text = "$it Agencies Downloaded"
        }

        syncViewModel.productsCount.observe(viewLifecycleOwner) {
            binding.pdtCount.text = "$it Products Downloaded"
        }

        syncViewModel.imageDownloadPer.observe(viewLifecycleOwner) {
            binding.run {
                if (pBar.visibility == View.VISIBLE) {
                    pBar.progress = it.first
                    dwnPerc.text = it.first.toString() + "%"
                }
                if (it.first == 100 || it.first == -1) {
                    btnSync2.isEnabled = true
                    btnSync3.isEnabled = true
                    btnSync4.isEnabled = true
                    btnSync4.text = "Sync Now"
                    pBar.visibility = View.GONE
                    dwnPerc.visibility = View.GONE
                }
                pdtImgCount.text = "Downloaded " + it.third + " of " + it.second + " images."
            }
        }

        syncViewModel.productImagesDataList.observe(viewLifecycleOwner) {
            (activity as SyncActivity).hideProgress()
            if (it is Result.Success && it.data.isNotEmpty()) {
                AppPreferences.serverImagesSize = it.data.size
                binding.apply {
                    btnSync2.isEnabled = false
                    btnSync3.isEnabled = false
                    btnSync4.text = "Syncing.."
                    btnSync4.isEnabled = false
                    pBar.visibility = View.VISIBLE
                    pBar.progress = 0
                    dwnPerc.visibility = View.VISIBLE
                    dwnPerc.text = "0%"
                    pdtImgCount.text = "Downloaded 0 of " + it.data.size + " images."
                }
                syncViewModel.downloadImages(requireActivity(), it.data)
            }
        }

        syncViewModel.pdtImagesCount.observe(viewLifecycleOwner) {
            binding.pdtImgCount.text =
                "Downloaded " + it + " of " + AppPreferences.serverImagesSize + " images."
        }
    }

    override fun onPause() {
        super.onPause()
        syncViewModel._productImagesDataList = MutableLiveData()
        syncViewModel._imageDownloadPer = MutableLiveData()
        syncViewModel._productsCount = MutableLiveData()
        syncViewModel._shopsCount = MutableLiveData()
        syncViewModel._shopsList = MutableLiveData()
        syncViewModel._productsList = MutableLiveData()
        syncViewModel._pdtImagesCount = MutableLiveData()

    }
}