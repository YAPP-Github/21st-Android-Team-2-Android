package com.yapp.itemfinder.domain.provider

interface UrlProvider {

    fun getApiPrefixUrl(): String

    fun getPrivacyHandleUrl(): String

    fun getTermsOfUseUrl(): String

}
