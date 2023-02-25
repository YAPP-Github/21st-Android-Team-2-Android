package com.yapp.itemfinder.provider

import com.yapp.itemfinder.BuildConfig
import com.yapp.itemfinder.domain.provider.BuildConfigProvider
import com.yapp.itemfinder.domain.provider.UrlProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUrlProvider @Inject constructor(
    private val buildConfigProvider: BuildConfigProvider
) : UrlProvider {

    override fun getApiPrefixUrl(): String = if (buildConfigProvider.isProd) BuildConfig.API_URL else BuildConfig.API_URL_DEV
    override fun getPrivacyHandleUrl(): String = BuildConfig.PRIVACY_HANDLE_URL

    override fun getTermsOfUseUrl(): String = BuildConfig.TERMS_OF_USE_URL

}
