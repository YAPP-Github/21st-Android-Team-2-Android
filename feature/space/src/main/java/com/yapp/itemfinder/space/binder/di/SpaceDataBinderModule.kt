package com.yapp.itemfinder.space.binder.di

import com.yapp.itemfinder.feature.common.datalist.binder.AddLockerSpaceBinder
import com.yapp.itemfinder.feature.common.datalist.binder.AddSpaceBinder
import com.yapp.itemfinder.feature.common.datalist.binder.ManageSpaceItemBinder
import com.yapp.itemfinder.space.binder.AddLockerSelectSpaceBinder
import com.yapp.itemfinder.space.binder.ManageSpaceAddSpaceBinder
import com.yapp.itemfinder.space.binder.ManageSpaceSpaceItemBinder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SpaceDataBinderModule {
    @Binds
    @Singleton
    abstract fun bindAddSpaceBinder(
        addSpaceBinder: ManageSpaceAddSpaceBinder
    ) : AddSpaceBinder

    @Binds
    @Singleton
    abstract fun bindSpaceItemBinder(
        spaceItemBinder: ManageSpaceSpaceItemBinder
    ): ManageSpaceItemBinder

    @Binds
    @Singleton
    abstract fun bindSelectSpaceBinder(
        selectSpaceBinder: AddLockerSelectSpaceBinder
    ): AddLockerSpaceBinder
}
