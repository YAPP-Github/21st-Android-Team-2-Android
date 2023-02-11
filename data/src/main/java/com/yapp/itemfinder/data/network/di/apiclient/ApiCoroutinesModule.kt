package com.yapp.itemfinder.data.network.di.apiclient

import com.yapp.itemfinder.data.network.api.AppApi
import com.yapp.itemfinder.data.network.api.managespace.ManageSpaceApi
import com.yapp.itemfinder.data.network.api.auth.AuthApi
import com.yapp.itemfinder.data.network.api.auth.AuthWithoutTokenApi
import com.yapp.itemfinder.data.network.api.home.HomeSpaceApi
import com.yapp.itemfinder.data.network.api.item.ItemApi
import com.yapp.itemfinder.data.network.api.lockerlist.LockerApi
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

    @Provides
    @Singleton
    fun provideManageSpaceApi(
        apiClient: ApiClient
    ): ManageSpaceApi {
        return apiClient.provideManageSpaceApi()
    }

    @Provides
    @Singleton
    fun provideHomeSpaceApi(
        apiClient: ApiClient
    ): HomeSpaceApi {
        return apiClient.provideHomeSpaceApi()
    }

    @Provides
    @Singleton
    fun provideLockerListApi(
        apiClient: ApiClient
    ): LockerApi {
        return apiClient.provideLockerListApi()
    }

    @Provides
    @Singleton
    fun provideItemApi(
        apiClient: ApiClient
    ): ItemApi {
        return apiClient.provideItemApi()
    }

}
