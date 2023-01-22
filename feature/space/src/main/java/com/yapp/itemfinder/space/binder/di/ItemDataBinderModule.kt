package com.yapp.itemfinder.space.binder.di

import com.yapp.itemfinder.feature.common.datalist.binder.AddItemCategoryBinder
import com.yapp.itemfinder.feature.common.datalist.binder.AddItemCountBinder
import com.yapp.itemfinder.space.binder.AddItemScreenCategoryBinder
import com.yapp.itemfinder.space.binder.AddItemScreenCountBinder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ItemDataBinderModule {
    @Binds
    @Singleton
    abstract fun bindAddItemCategoryBinder(
        addItemCategoryBinder: AddItemScreenCategoryBinder
    ): AddItemCategoryBinder

    @Binds
    @Singleton
    abstract fun bindAddItemCountBinder(
        addItemCountBinder: AddItemScreenCountBinder
    ): AddItemCountBinder
}
