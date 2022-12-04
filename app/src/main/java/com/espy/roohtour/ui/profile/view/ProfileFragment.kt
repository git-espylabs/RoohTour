package com.espy.roohtour.ui.profile.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.espy.roohtour.R
import com.espy.roohtour.api.Result
import com.espy.roohtour.app.AppSettings
import com.espy.roohtour.app.InstanceManager
import com.espy.roohtour.databinding.FragmentProfileBinding
import com.espy.roohtour.extensions.launchActivity
import com.espy.roohtour.preference.AppPreferences
import com.espy.roohtour.ui.base.BaseFragmentWithBinding


class ProfileFragment:
    BaseFragmentWithBinding<FragmentProfileBinding>(R.layout.fragment_profile),
    View.OnClickListener {

    private lateinit var profileViewMoel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileViewMoel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = profileViewMoel
            viewParent = this@ProfileFragment

            Glide
                .with(this@ProfileFragment)
                .load(AppSettings.endPoints.IMAGE_ASSETS + InstanceManager.profile?.image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(profiledp)

            tvName.text = InstanceManager.profile?.staff_name
            tvEmail.text = InstanceManager.profile?.email
        }

        setObserver()
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.tvEditProfile ->{

            }
            R.id.tvApplyLeave ->{
                ApplyLeaveBottomSheetDialogFragment(profileViewMoel).show(
                    childFragmentManager,
                    "ApplyLeaveFragment"
                )
            }
            R.id.tvAddExpense ->{
                AddExpenseBottomSheetDialogFragment(profileViewMoel).show(
                    childFragmentManager,
                    "AddExpenseFragment"
                )
            }
            R.id.tvLogout ->{
                AppPreferences.userId = ""
                activity?.launchActivity<LoginActivity>(){
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                activity?.finish()
            }
        }
    }

    private fun setObserver(){
        profileViewMoel.leaveApplicationResponse.observe(viewLifecycleOwner, {
            if (it){
                showToast(R.string.leave_apply_success)
            }else{
                showToast(R.string.something_went_wrong)
            }
        })

        profileViewMoel.expenseTypesList.observe(viewLifecycleOwner, {
            if (it is Result.Success && it.data.any()){
                InstanceManager.capExpenseTypes = it.data
            }
        })
        profileViewMoel.addExpenseStatus.observe(viewLifecycleOwner, {
            if (it){
                showToast("Expense added successfully!")
            }else{
                showToast(R.string.something_went_wrong)
            }
        })

    }
}