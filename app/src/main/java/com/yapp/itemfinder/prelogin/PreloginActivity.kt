package com.yapp.itemfinder.prelogin

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.yapp.itemfinder.databinding.AcitivityPreloginBinding
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.home.HomeActivity
import kotlinx.coroutines.Job

class PreloginActivity: BaseStateActivity<PreloginViewModel, AcitivityPreloginBinding>() {

    override val vm by viewModels<PreloginViewModel>()

    override val binding by viewBinding(AcitivityPreloginBinding::inflate)

    override fun initViews() = with(binding) {


    }

    override fun observeData(): Job {
        return Job()
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, PreloginActivity::class.java)

    }


}
