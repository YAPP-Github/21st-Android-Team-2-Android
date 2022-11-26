package com.yapp.itemfinder.data.network.di.apiclient

import com.yapp.itemfinder.data.network.api.AppApi
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
    fun provideAppApi(apiClient: ApiClient): AppApi {
        return apiClient.provideAppApi()
    }

}
