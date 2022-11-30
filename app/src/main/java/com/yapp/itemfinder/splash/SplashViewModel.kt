package com.yapp.itemfinder.splash

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.feature.common.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow

class SplashViewModel: BaseViewModel() {

    val splashEventSharedFlow = MutableSharedFlow<SplashEvent>()

    /**
     * This is the splash delay test
     */
    fun readyToStart() = viewModelScope.launch {
        delay(2000L)
        splashEventSharedFlow.emit(SplashEvent.StartHome)
    }

}
