package com.yapp.itemfinder.data.network.di.apiclient

import com.google.gson.Gson
import com.yapp.itemfinder.data.network.api.auth.AuthWithoutTokenApi
import com.yapp.itemfinder.data.network.di.okhttp.OkHttpClientWithoutAuthenticatorQualifier
import com.yapp.itemfinder.domain.di.ApiGsonQualifier
import com.yapp.itemfinder.domain.provider.UrlProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClientWithoutAuthenticator @Inject constructor(
    private val urlProvider: UrlProvider,
    @ApiGsonQualifier
    private val apiGson: Gson
) {

    @OkHttpClientWithoutAuthenticatorQualifier
    @Inject
    lateinit var okHttpClientWithoutAuthenticator: OkHttpClient

    private fun createAdapterWithoutAuthenticator(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClientWithoutAuthenticator)
            .addConverterFactory(GsonConverterFactory.create(apiGson))
            .build()
    }

    /**
     * api without authenticator
     */
    private val apiAdapter: Retrofit by lazy {
        createAdapterWithoutAuthenticator(urlProvider.getApiPrefixUrl())
    }

    /**
     * Api List
     */
    fun provideAuthWithoutTokenApi(): AuthWithoutTokenApi =
        apiAdapter.create(AuthWithoutTokenApi::class.java)

}
