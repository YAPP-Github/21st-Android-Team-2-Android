package com.yapp.itemfinder.splash

import com.yapp.itemfinder.feature.common.SideEffect

sealed class SplashScreenSideEffect : SideEffect {
    object StartHome : SplashScreenSideEffect()
    object StartPrelogin : SplashScreenSideEffect()
}
