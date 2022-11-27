package com.yapp.itemfinder.deeplink

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnPreDrawListener
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.yapp.itemfinder.databinding.ActivityDeeplinkBinding
import com.yapp.itemfinder.feature.common.BaseActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.coroutines.coroutineExceptionHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class DeeplinkActivity : BaseActivity<DeeplinkViewModel, ActivityDeeplinkBinding>(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + coroutineExceptionHandler

    override val vm by viewModels<DeeplinkViewModel>()

    override val binding by viewBinding(ActivityDeeplinkBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { vm.keepScreen }
        super.onCreate(savedInstanceState)
    }

    override fun preload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1f, 0f).run {
                    interpolator = AnticipateInterpolator()
                    duration = 200L
                    doOnEnd { splashScreenView.remove() }
                    start()
                }
            }
        }

        binding.root.viewTreeObserver.addOnPreDrawListener(
            object : OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (vm.keepScreen.not()) {
                        // The content is ready; start drawing.
                        binding.root.viewTreeObserver?.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }

            }
        )

        vm.readyToStart()
    }

    override fun initViews() {
        //TODO("Not yet implemented")
    }

    override fun observeData(): Job {
        //TODO("Not yet implemented")
        return Job()
    }

}
