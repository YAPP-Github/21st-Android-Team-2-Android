package com.yapp.itemfinder.data.network.di.okhttp

import androidx.viewbinding.BuildConfig
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yapp.itemfinder.data.network.header.HeaderKey
import com.yapp.itemfinder.data.network.mapper.DataMapper
import com.yapp.itemfinder.data.network.response.ErrorResultEntity
import com.yapp.itemfinder.domain.data.SecureLocalData
import com.yapp.itemfinder.domain.data.SecureLocalDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
class OkHttpInterceptorModule {

    @HeaderInterceptorQualifier
    @Provides
    fun provideBanksaladHeaderInterceptor(
        secureLocalDataStore: SecureLocalDataStore
    ): Interceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
            .header(HeaderKey.CONTENT_TYPE_HEADER_KEY, HeaderKey.CONTENT_TYPE_HEADER_VALUE)
        val accessToken = secureLocalDataStore.get(SecureLocalData.AccessToken)
        if (accessToken.isNotEmpty()) {
            requestBuilder.header(HeaderKey.AUTHORIZATION_HEADER_KEY, "${HeaderKey.BEARER_PREFIX} $accessToken")
        }
        chain.proceed(requestBuilder.build())

    }

    @HttpLoggingInterceptorQualifier
    @Provides
    fun provideHttpLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

    @DataParseInterceptorQualifier
    @Provides
    fun provideDataParseInterceptorQualifier(
        dataMapper: DataMapper
    ): Interceptor = Interceptor { chain ->
        val gson = Gson()
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
        val response = chain.proceed(request)
        val responseData = if (response.isSuccessful) {

            val rawJson =
                if (response.body?.string() == "okay") "{}"
                else response.body?.string() ?: "{}"
            val jsonObject = gson.fromJson(rawJson, JsonObject::class.java)
            if (jsonObject.isJsonArray) {
                /**
                 * [
                 *   {id, type=food},
                 *   {id, type=필통},
                 *   {id, type},
                 *   {id, type},
                 * ]
                 */
                jsonObject.asJsonArray.map {
                    dataMapper.map(it.asJsonObject)
                }
            } else {
                /**
                 * {id, type}
                 */
                dataMapper.map(jsonObject) ?: jsonObject
            }
        } else {
            val rawJson = response.body?.string() ?: "{}"
            gson.fromJson(rawJson, ErrorResultEntity::class.java)
        }

        response.newBuilder()
            .body(gson.toJson(responseData).toResponseBody())
            .build()
    }
}

