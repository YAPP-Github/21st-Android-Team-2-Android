package com.yapp.itemfinder.space.binder.di

import com.yapp.itemfinder.feature.common.datalist.binder.*
import com.yapp.itemfinder.space.binder.*
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

    @Binds
    @Singleton
    abstract fun bindAddLockerPhotoItem(
        addLockerPhoto: LockerAddLockerPhotoItemBinder
    ): AddLockerPhotoItemBinder

    @Binds
    @Singleton
    abstract fun bindAddLockerNameBinder(
        addLockerNameBinder: AddLockerScreenNameBinder
    ): AddLockerNameBinder

    @Binds
    @Singleton
    abstract fun bindAddLockerIconBinder(
        addLockerIconBinder: AddLockerScreenIconBinder
    ): AddLockerIconBinder
}
