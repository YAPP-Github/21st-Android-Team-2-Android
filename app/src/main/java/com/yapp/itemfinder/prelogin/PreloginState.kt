package com.yapp.itemfinder.prelogin

import com.yapp.itemfinder.feature.common.State

sealed class PreloginState: State {

    object Uninitialized: PreloginState()

    object Loading: PreloginState()

    object Success: PreloginState()

    object Error: PreloginState()

}
