package com.yapp.itemfinder.home.binder.di

import com.yapp.itemfinder.feature.common.datalist.binder.LikeItemBinder
import com.yapp.itemfinder.feature.common.datalist.binder.di.ContainerLikeItemQualifier
import com.yapp.itemfinder.feature.common.datalist.binder.di.HomeLikeItemQualifier
import com.yapp.itemfinder.home.binder.HomeLikeItemBinder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeDataBinderModule {

    @Binds
    @Singleton
    @HomeLikeItemQualifier
    abstract fun bindLikeItemBinder(
        homeLikeItemBinder: HomeLikeItemBinder
    ): LikeItemBinder

}

@Module
@InstallIn(SingletonComponent::class)
abstract class ContainerDataBinderModule {

    @Binds
    @Singleton
    @ContainerLikeItemQualifier
    abstract fun bindLikeItemBinder(
        homeLikeItemBinder: HomeLikeItemBinder
    ): LikeItemBinder

}
