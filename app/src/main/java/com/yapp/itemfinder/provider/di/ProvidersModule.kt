package com.yapp.itemfinder.provider.di

import com.yapp.itemfinder.domain.provider.UrlProvider
import com.yapp.itemfinder.domain.coroutines.DispatcherProvider
import com.yapp.itemfinder.domain.provider.AppNavigateProvider
import com.yapp.itemfinder.domain.provider.BuildConfigProvider
import com.yapp.itemfinder.provider.DefaultAppNavigateProvider
import com.yapp.itemfinder.provider.DefaultBuildConfigProvider
import com.yapp.itemfinder.provider.DefaultDispatcherProvider
import com.yapp.itemfinder.provider.DefaultUrlProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ProvidersModule {

    @Binds
    @Singleton
    abstract fun bindUrlProvider(
        defaultUrlProvider: DefaultUrlProvider,
    ): UrlProvider

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        defaultDispatcher: DefaultDispatcherProvider
    ): DispatcherProvider

    @Binds
    @Singleton
    abstract fun bindBuildConfigProvider(
        defaultBuildConfigProvider: DefaultBuildConfigProvider
    ): BuildConfigProvider

    @Binds
    @Singleton
    abstract fun bindAppNavigateProvider(
        defaultAppNavigateProvider: DefaultAppNavigateProvider
    ): AppNavigateProvider

}

