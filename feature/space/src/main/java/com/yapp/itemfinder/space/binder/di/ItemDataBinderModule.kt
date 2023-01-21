package com.yapp.itemfinder.space.binder.di

import com.yapp.itemfinder.feature.common.datalist.binder.AddItemBinder
import com.yapp.itemfinder.space.binder.AddItemCategoryBinder
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
        addItemCategoryBinder: AddItemCategoryBinder
    ): AddItemBinder
}
