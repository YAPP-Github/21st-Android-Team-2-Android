package com.yapp.itemfinder.data.network.di.apiclient

import com.yapp.itemfinder.data.network.api.AppApi
import com.yapp.itemfinder.data.network.api.auth.AuthApi
import com.yapp.itemfinder.data.network.api.auth.AuthWithoutTokenApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ApiCoroutinesModule {

    @Provides
    @Singleton
    fun provideAppApi(
        apiClient: ApiClient
    ): AppApi {
        return apiClient.provideAppApi()
    }

    @Provides
    @Singleton
    fun provideAuthWithoutTokenApi(
        apiClientWithoutAuthenticator: ApiClientWithoutAuthenticator
    ): AuthWithoutTokenApi {
        return apiClientWithoutAuthenticator.provideAuthWithoutTokenApi()
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        apiClient: ApiClient
    ): AuthApi {
        return apiClient.provideAuthApi()
    }

}
