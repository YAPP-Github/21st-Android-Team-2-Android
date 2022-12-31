package com.yapp.itemfinder.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.databinding.ActivitySplashBinding
import com.yapp.itemfinder.feature.common.BaseActivity
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.coroutines.coroutineExceptionHandler
import com.yapp.itemfinder.utility.DefaultScreenNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseStateActivity<SplashViewModel, ActivitySplashBinding>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + coroutineExceptionHandler

    override val vm by viewModels<SplashViewModel>()

    override val binding by viewBinding(ActivitySplashBinding::inflate)

    @Inject
    lateinit var screenNavigator: DefaultScreenNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun initViews() {
    }

    override fun observeData(): Job = lifecycleScope.launch {
        launch {
            vm.stateFlow.collect { state ->
                when (state) {
                    is SplashScreenState.Uninitialized -> Unit
                    is SplashScreenState.Loading -> handleLoading(state)
                    is SplashScreenState.Success -> handleSuccess(state)
                    is SplashScreenState.Error -> handleError(state)
                }
            }
        }
        launch {
            vm.sideEffectFlow.collect { sideEffect ->
                when (sideEffect) {
                    is SplashScreenSideEffect.StartHome -> {
                        startActivity(screenNavigator.newIntentHomeActivity(this@SplashActivity))
                        finish()
                    }
                }
            }
        }
    }

    private fun handleLoading(splashScreenState: SplashScreenState) {

    }

    private fun handleSuccess(splashScreenState: SplashScreenState.Success) {
        vm.startHome()
    }

    private fun handleError(splashScreenState: SplashScreenState) {

    }

}
