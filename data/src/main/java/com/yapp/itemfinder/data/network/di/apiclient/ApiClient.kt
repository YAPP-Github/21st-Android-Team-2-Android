package com.yapp.itemfinder.data.network.di.apiclient

import com.google.gson.Gson
import com.yapp.itemfinder.data.network.api.AppApi
import com.yapp.itemfinder.data.network.api.auth.AuthApi
import com.yapp.itemfinder.data.network.di.okhttp.OkHttpClientQualifier
import com.yapp.itemfinder.domain.di.ApiGsonQualifier
import com.yapp.itemfinder.domain.provider.UrlProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor(
    @ApiGsonQualifier
    private val apiGson: Gson,
    private val urlProvider: UrlProvider
) {

    @OkHttpClientQualifier
    @Inject
    lateinit var okHttpClient: OkHttpClient

    private fun createApiAdapter(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(apiGson))
            .build()
    }

    /**
     * Api Adapter
     */
    private val apiAdapter: Retrofit by lazy {
        createApiAdapter(urlProvider.getApiPrefixUrl())
    }

    /**
     * Api List
     */
    fun provideAppApi(): AppApi =
        apiAdapter.create(AppApi::class.java)

    fun provideAuthApi(): AuthApi =
        apiAdapter.create(AuthApi::class.java)

}
