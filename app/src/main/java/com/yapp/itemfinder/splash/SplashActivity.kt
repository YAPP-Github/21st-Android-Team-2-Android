package com.yapp.itemfinder.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.databinding.ActivitySplashBinding
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.Depth
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

    override val depth: Depth
        get() = Depth.FIRST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun initViews() = Unit

    override fun observeData(): Job = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                        is SplashScreenSideEffect.ShowErrorToast -> {
                            if (sideEffect.message.isNullOrEmpty().not()) {
                                Toast.makeText(this@SplashActivity, sideEffect.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

}
