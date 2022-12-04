package com.espy.roohtour.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.activityViewModels
import com.espy.roohtour.R
import com.espy.roohtour.app.InstanceManager
import com.espy.roohtour.databinding.FragmentHomeBinding
import com.espy.roohtour.extensions.launchActivity
import com.espy.roohtour.extensions.setImageTint
import com.espy.roohtour.ui.attendance.view.AttendanceActivity
import com.espy.roohtour.ui.base.BaseFragmentWithBinding
import com.espy.roohtour.ui.shops.view.AddNewShopActivty
import com.espy.roohtour.ui.shops.view.OrderHistoryActivity
import com.espy.roohtour.ui.sync.view.SyncActivity


class HomeFragment:
    BaseFragmentWithBinding<FragmentHomeBinding>(R.layout.fragment_home), View.OnClickListener {

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = homeViewModel
            viewParent = this@HomeFragment
        }

        (activity as HomeActivity).setToolBarProperties(InstanceManager.profile?.staff_name?:"", isHome = true)

        setCardsOnHome()

        getVersion()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.actionLogout).isVisible = true
        menu.findItem(R.id.actionSearch).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    private fun setCardsOnHome(){
        binding.apply {

            card1.run {
                ivItemIcon.setImageResource(R.drawable.ic_checked_in)
                tvItemTitle.text = resources.getString(R.string.punch_in_now)
                tvItemDescription.text = resources.getString(R.string.mark_attendance)
            }

            card2.run {
                ivItemIcon.setImageResource(R.drawable.ic_sync)
                tvItemTitle.text = resources.getString(R.string.sync_all_data)
                tvItemDescription.text = resources.getString(R.string.update_route_prompt)
            }
//
//            card3.run {
//                ivItemIcon.setImageResource(R.drawable.ic_rupee)
//                tvItemTitle.text = resources.getString(R.string.add_expense)
//                tvItemDescription.text = resources.getString(R.string.add_daily_expense)
//            }
//
            card4.run {
                ivItemIcon.setImageResource(R.drawable.ic_shop)
                ivItemIcon.setImageTint(R.color.icon_tint_base)
                tvItemTitle.text = resources.getString(R.string.add_shop)
                tvItemDescription.text = resources.getString(R.string.add_new_shop)
            }
//
            card5.run {
                ivItemIcon.setImageResource(R.drawable.ic_history)
                ivItemIcon.setImageTint(R.color.icon_tint_base)
                tvItemTitle.text = resources.getString(R.string.order_history)
                tvItemDescription.text = resources.getString(R.string.order_history_desc)
            }

        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.card1 ->{
                activity?.launchActivity<AttendanceActivity>()
            }
            R.id.card2 ->{
                activity?.launchActivity<SyncActivity>()
            }
//            R.id.card3 ->{
//                AddExpenseBottomSheetDialogFragment(homeViewModel).show(
//                    childFragmentManager,
//                    "AddExpenseFragment"
//                )
//            }
            R.id.card4 ->{
                activity?.launchActivity<AddNewShopActivty>()
            }
            R.id.card5 ->{
                activity?.launchActivity<OrderHistoryActivity>()
            }
        }
    }

    private fun getVersion(){
        val manager = requireContext().packageManager
        val info = manager.getPackageInfo(
            requireContext().packageName, 0
        )
        val version = info.versionName
        binding.tvVersion.text = "v$version"
    }


}