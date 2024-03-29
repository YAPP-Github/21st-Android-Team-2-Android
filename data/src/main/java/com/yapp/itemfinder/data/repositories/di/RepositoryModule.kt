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

    @LockerMockRepositoryQualifiers
    @Binds
    @Singleton
    abstract fun bindMockLockerRepository(repository: LockerMockRepositoryImpl): LockerRepository

    @LockerRepositoryQualifiers
    @Binds
    @Singleton
    abstract fun bindLockerRepository(repository: LockerRepositoryImpl): LockerRepository

    @Binds
    @Singleton
    abstract fun bindManageSpaceRepository(repository: ManageSpaceRepositoryImpl): ManageSpaceRepository

    @SpaceMockRepositoryQualifiers
    @Binds
    @Singleton
    abstract fun bindSpaceMockRepository(repository: SpaceMockRepositoryImpl): SpaceRepository

    @SpaceRepositoryQualifiers
    @Binds
    @Singleton
    abstract fun bindSelectSpaceRepository(repository: SelectSpaceRepositoryImpl): SpaceRepository

    @ItemRepositoryQualifiers
    @Binds
    @Singleton
    abstract fun bindItemRepository(repository: ItemRepositoryImpl): ItemRepository

    @ItemMockRepositoryQualifiers
    @Binds
    @Singleton
    abstract fun bindItemMockRepository(repository: ItemMockRepositoryImpl): ItemRepository

    @Binds
    @Singleton
    abstract fun bindImageRepository(repository: ImageRepositoryImpl): ImageRepository

    @Binds
    @Singleton
    abstract fun bindTagRepository(repository: TagRepositoryImpl): TagRepository

}
