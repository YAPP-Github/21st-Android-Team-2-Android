package com.yapp.itemfinder.locker.binder.di

import com.yapp.itemfinder.feature.common.datalist.binder.AddLockerItemBinder
import com.yapp.itemfinder.feature.common.datalist.binder.LockerItemBinder
import com.yapp.itemfinder.locker.binder.LockerAddLockerItemBinder
import com.yapp.itemfinder.locker.binder.LockerLockerItemBinder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LockerDataBinderModule {
    @Binds
    @Singleton
    abstract fun bindLockerItemBinder(
        homeLockerItemBinder: LockerLockerItemBinder
    ): LockerItemBinder

    @Binds
    @Singleton
    abstract fun bindAddLockerBinder(
        homeAddLockerItemBinder: LockerAddLockerItemBinder
    ): AddLockerItemBinder
}
