package com.yapp.itemfinder.prelogin

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.databinding.AcitivityPreloginBinding
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.home.HomeActivity
import com.yapp.itemfinder.home.tabs.like.LikeTabSideEffect
import com.yapp.itemfinder.home.tabs.like.LikeTabState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PreloginActivity : BaseStateActivity<PreloginViewModel, AcitivityPreloginBinding>() {

    override val vm by viewModels<PreloginViewModel>()

    override val binding by viewBinding(AcitivityPreloginBinding::inflate)

    override fun initViews() = with(binding) {
        kakaoLoginButton.setOnClickListener {
            vm.requestLogin()
        }
    }

    override fun observeData(): Job =
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.sideEffectFlow.collect { sideEffect ->
                        when (sideEffect) {
                            is PreloginSideEffect.RequestKakaoLogin -> handleRequestKakaoLogin()
                        }
                    }
                }
            }
        }

    private fun handleRequestKakaoLogin() {
        
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, PreloginActivity::class.java)

    }


}
