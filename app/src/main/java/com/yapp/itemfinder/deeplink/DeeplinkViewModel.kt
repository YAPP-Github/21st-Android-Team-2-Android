package com.yapp.itemfinder.deeplink

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.feature.common.BaseViewModel
import kotlinx.coroutines.*

class DeeplinkViewModel: BaseViewModel() {

    var keepScreen: Boolean = true

    /**
     * This is the splash delay test
     */
    fun readyToStart() = viewModelScope.launch {
        delay(2000L)
        keepScreen = false
    }

}
