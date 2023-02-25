package com.yapp.itemfinder.provider

import com.yapp.itemfinder.BuildConfig
import com.yapp.itemfinder.domain.provider.UrlProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUrlProvider @Inject constructor() : UrlProvider {

    override fun getApiPrefixUrl(): String = BuildConfig.API_URL
    override fun getPrivacyHandleUrl(): String = BuildConfig.PRIVACY_HANDLE_URL

    override fun getTermsOfUseUrl(): String = BuildConfig.TERMS_OF_USE_URL

}
