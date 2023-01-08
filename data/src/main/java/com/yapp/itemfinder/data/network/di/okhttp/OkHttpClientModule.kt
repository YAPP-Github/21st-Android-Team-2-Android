package com.yapp.itemfinder.data.network.di.okhttp

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class OkHttpClientModule {

    companion object {
        private const val CONNECT_TIMEOUT_MILLIS = 3000L
    }

    @OkHttpClientQualifier
    @Provides
    fun provideOkHttpClientBuilder(
        @HeaderInterceptorQualifier headerInterceptor: Interceptor,
        @HttpLoggingInterceptorQualifier httpLoggingInterceptor: Interceptor,
        @DataParseInterceptorQualifier dataParseInterceptor: Interceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(dataParseInterceptor)
        .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .build()

    @OkHttpClientWithoutAuthenticatorQualifier
    @Provides
    fun provideOkHttpClientWithoutAuthenticatorBuilder(
        @HttpLoggingInterceptorQualifier httpLoggingInterceptor: Interceptor,
        @DataParseInterceptorQualifier dataParseInterceptor: Interceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(dataParseInterceptor)
        .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
        .build()


}
