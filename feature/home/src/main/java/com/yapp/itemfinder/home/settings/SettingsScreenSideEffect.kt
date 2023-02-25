package com.yapp.itemfinder.home.settings

import com.yapp.itemfinder.feature.common.SideEffect

sealed class SettingsScreenSideEffect : SideEffect {

    data class ShowLoading(
        val isShow: Boolean
    ) : SettingsScreenSideEffect()

    sealed class Start: SettingsScreenSideEffect() {

        data class PrivacyHandle(
            val url: String,
        ) : Start()

        data class TermsOfUse(
            val url: String,
        ) : Start()

        object OpenSource : Start()

        data class ContactUs(
            val email: String,
            val subject: String,
            val text: String,
        ) : Start()

        object ClearAndGoToPreLogin : Start()

    }

    data class ShowErrorToast(
        val message: String?
    ) : SettingsScreenSideEffect()

}
