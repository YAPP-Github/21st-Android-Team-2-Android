package com.yapp.itemfinder.data.repositories.di

import com.yapp.itemfinder.data.repositories.BannerMockRepositoryImpl
import com.yapp.itemfinder.data.repositories.LockerMockRepositoryImpl
import com.yapp.itemfinder.data.repositories.ManageSpaceRepositoryImpl
import com.yapp.itemfinder.data.repositories.SpaceMockRepositoryImpl
import com.yapp.itemfinder.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMockSpaceRepository(repository: SpaceMockRepositoryImpl): HomeSpaceRepository

    @Binds
    @Singleton
    abstract fun bindMockBannerRepository(repository: BannerMockRepositoryImpl): BannerRepository

    @Binds
    @Singleton
    abstract fun bindMockLockerRepository(repository: LockerMockRepositoryImpl): LockerRepository

    @Binds
    @Singleton
    abstract fun bindManageSpaceRepository(repository: ManageSpaceRepositoryImpl): ManageSpaceRepository
}
