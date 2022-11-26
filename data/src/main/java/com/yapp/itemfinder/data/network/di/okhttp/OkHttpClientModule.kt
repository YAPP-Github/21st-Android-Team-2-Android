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
        @HttpLoggingInterceptorQualifier httpLoggingInterceptor: Interceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

}
