package com.yapp.itemfinder.data.repositories.di

import com.yapp.itemfinder.data.preference.DefaultSecureLocalDataStore
import com.yapp.itemfinder.domain.data.SecureLocalDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataStoreModule {

    @Binds
    @Singleton
    abstract fun bindSecureLocalDataStore(
        defaultSecureLocalDataStore: DefaultSecureLocalDataStore
    ): SecureLocalDataStore

}

