package com.yapp.itemfinder.data.repositories.di

import com.yapp.itemfinder.data.repositories.DefaultAppRepository
import com.yapp.itemfinder.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    @Singleton
    abstract fun bindAppRepository(
        defaultAppRepository: DefaultAppRepository
    ): AppRepository

}

