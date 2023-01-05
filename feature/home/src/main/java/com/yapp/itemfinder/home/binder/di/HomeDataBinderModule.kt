package com.yapp.itemfinder.home.binder.di

import com.yapp.itemfinder.feature.common.datalist.binder.*
import com.yapp.itemfinder.feature.common.datalist.binder.di.HomeLikeItemQualifier
import com.yapp.itemfinder.feature.common.datalist.binder.di.HomeMySpaceUpperCellItemQualifier
import com.yapp.itemfinder.home.binder.*
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

    @Binds
    @Singleton
    abstract fun bindSpaceItemBInder(
        homeSpaceItemBinder: HomeSpaceItemBinder
    ): SpaceItemBinder

    @Binds
    @Singleton
    @HomeMySpaceUpperCellItemQualifier
    abstract fun homeMySpaceUpperCellItemBinder(
        mySpaceUpperCellItemBinder: HomeMySpaceUpperCellItemBinder
    ): CellItemBinder

}
