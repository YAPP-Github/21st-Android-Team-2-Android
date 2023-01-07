package com.yapp.itemfinder.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.databinding.ActivitySplashBinding
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.coroutines.coroutineExceptionHandler
import com.yapp.itemfinder.home.HomeActivity
import com.yapp.itemfinder.prelogin.PreloginActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun initViews() = Unit

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
                    is SplashScreenSideEffect.StartPrelogin -> {
                        startActivity(PreloginActivity.newIntent(this@SplashActivity))
                        finish()
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
