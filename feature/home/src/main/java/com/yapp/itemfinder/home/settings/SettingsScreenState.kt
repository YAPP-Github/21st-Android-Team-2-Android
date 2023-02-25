package com.yapp.itemfinder.home.settings

import com.yapp.itemfinder.feature.common.State

sealed class SettingsScreenState : State {

    object Uninitialized : SettingsScreenState()

    data class Success(
        val appVersion: String
    ) : SettingsScreenState()

    data class Error(
        val e: Throwable
    ) : SettingsScreenState()
}
