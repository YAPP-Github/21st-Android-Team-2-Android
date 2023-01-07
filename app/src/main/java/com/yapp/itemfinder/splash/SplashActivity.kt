package com.yapp.itemfinder.splash

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.databinding.ActivitySplashBinding
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.exception.coroutineExceptionHandler
import com.yapp.itemfinder.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseStateActivity<SplashViewModel, ActivitySplashBinding>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + coroutineExceptionHandler

    override val vm by viewModels<SplashViewModel>()

    override val binding by viewBinding(ActivitySplashBinding::inflate)


    override fun initViews() {

    }

    override fun observeData(): Job = lifecycleScope.launch {
        launch {
            vm.stateFlow.collect { state ->
                when (state) {
                    is SplashScreenState.Uninitialized -> Unit
                    is SplashScreenState.Success -> handleSuccess(state)
                    is SplashScreenState.Error -> handleError(state)
                }
            }
        }
        launch {
            vm.sideEffectFlow.collect { sideEffect ->
                when (sideEffect) {
                    is SplashScreenSideEffect.StartHome -> {
                        startActivity(HomeActivity.newIntent(this@SplashActivity))
                        finish()
                    }
                    is SplashScreenSideEffect.StartSignUp -> {
                        // TODO: 회원가입 페이지로 이동
                    }
                }
            }
        }
    }

    private fun handleSuccess(splashScreenState: SplashScreenState.Success) {

    }

    private fun handleError(splashScreenState: SplashScreenState) {

    }

}
