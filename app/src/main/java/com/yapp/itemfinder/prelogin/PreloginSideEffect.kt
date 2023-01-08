package com.yapp.itemfinder.prelogin

import com.yapp.itemfinder.feature.common.SideEffect

sealed class PreloginSideEffect: SideEffect {

    object RequestKakaoLogin: PreloginSideEffect()

}
