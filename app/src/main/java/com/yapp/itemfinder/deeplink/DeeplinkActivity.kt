package com.yapp.itemfinder.deeplink

import android.os.Bundle
import android.view.ViewTreeObserver.OnPreDrawListener
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.databinding.ActivityDeeplinkBinding
import com.yapp.itemfinder.feature.common.BaseActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.coroutines.coroutineExceptionHandler
import com.yapp.itemfinder.feature.common.extension.findContentView
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
        super.onCreate(savedInstanceState)
    }

    override fun preload() {
        findContentView()?.viewTreeObserver?.addOnPreDrawListener(
            object : OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (vm.isReadyToStart()) {
                        // The content is ready; start drawing.
                        afterPreDraw(this)
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }

            }
        )
    }

    private fun afterPreDraw(preDrawListener: OnPreDrawListener) {
        lifecycleScope.launch {
            delay(2000L)
            findContentView()?.viewTreeObserver?.removeOnPreDrawListener(preDrawListener)
        }
    }

    override fun initViews() {
        //TODO("Not yet implemented")
    }

    override fun observeData(): Job {
        //TODO("Not yet implemented")
        return Job()
    }

}
