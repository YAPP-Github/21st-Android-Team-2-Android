package com.yapp.itemfinder.data.repositories.di

import com.yapp.itemfinder.data.repositories.*
import com.yapp.itemfinder.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @HomeSpaceMockRepositoryQualifier
    @Binds
    @Singleton
    abstract fun bindHomeSpaceMockMockRepository(repository: HomeSpaceMockRepositoryImpl): HomeSpaceRepository

    @HomeSpaceRepositoryQualifier
    @Binds
    @Singleton
    abstract fun bindHomeSpaceRepository(repository: HomeSpaceRepositoryImpl): HomeSpaceRepository

    @Binds
    @Singleton
    abstract fun bindMockBannerRepository(repository: BannerMockRepositoryImpl): BannerRepository

    @Binds
    @Singleton
    abstract fun bindMockLockerRepository(repository: LockerMockRepositoryImpl): LockerRepository

    @Binds
    @Singleton
    abstract fun bindManageSpaceRepository(repository: ManageSpaceRepositoryImpl): ManageSpaceRepository

    @Binds
    @Singleton
    abstract fun bindThingsRepository(repository: ThingMockRepositoryImpl): ThingRepository
}
