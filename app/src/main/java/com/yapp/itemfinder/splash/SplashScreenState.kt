package com.yapp.itemfinder.splash

import com.yapp.itemfinder.feature.common.State

sealed class SplashScreenState : State {

    object Uninitialized : SplashScreenState()

    object Success : SplashScreenState()

    data class Error(
        val e: Throwable
    ) : SplashScreenState()
}
