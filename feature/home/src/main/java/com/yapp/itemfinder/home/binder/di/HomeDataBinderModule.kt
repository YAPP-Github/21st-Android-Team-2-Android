package com.yapp.itemfinder.home.binder.di

import com.yapp.itemfinder.feature.common.datalist.binder.CellItemBinder
import com.yapp.itemfinder.feature.common.datalist.binder.LikeItemBinder
import com.yapp.itemfinder.feature.common.datalist.binder.LockerItemBinder
import com.yapp.itemfinder.feature.common.datalist.binder.SpaceItemBinder
import com.yapp.itemfinder.feature.common.datalist.binder.di.HomeLikeItemQualifier
import com.yapp.itemfinder.feature.common.datalist.binder.di.HomeMySpaceUpperCellItemQualifier
import com.yapp.itemfinder.home.binder.HomeLikeItemBinder
import com.yapp.itemfinder.home.binder.HomeMySpaceUpperCellItemBinder
import com.yapp.itemfinder.home.binder.HomeLockerItemBinder
import com.yapp.itemfinder.home.binder.HomeSpaceItemBinder
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

    @Binds
    @Singleton
    abstract fun bindLockerItemBinder(
        homeLockerItemBinder: HomeLockerItemBinder
    ): LockerItemBinder

}
