package com.yapp.itemfinder.prelogin

import androidx.activity.viewModels
import com.yapp.itemfinder.databinding.AcitivityPreloginBinding
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import kotlinx.coroutines.Job

class PreloginActivity: BaseStateActivity<PreloginViewModel, AcitivityPreloginBinding>() {

    override val vm by viewModels<PreloginViewModel>()

    override val binding by viewBinding(AcitivityPreloginBinding::inflate)

    override fun initViews() = with(binding) {


    }

    override fun observeData(): Job {
        return Job()
    }


}
