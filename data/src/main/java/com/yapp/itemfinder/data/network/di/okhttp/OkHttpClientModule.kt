package com.yapp.itemfinder.data.network.di.okhttp

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
class OkHttpClientModule {

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
        .build()

    @OkHttpClientWithoutAuthenticatorQualifier
    @Provides
    fun provideOkHttpClientWithoutAuthenticatorBuilder(
        @HttpLoggingInterceptorQualifier httpLoggingInterceptor: Interceptor,
        @DataParseInterceptorQualifier dataParseInterceptor: Interceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(dataParseInterceptor)
        .build()


}
