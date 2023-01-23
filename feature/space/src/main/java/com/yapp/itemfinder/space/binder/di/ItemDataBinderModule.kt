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

    @Binds
    @Singleton
    abstract fun bindAddItemAdditionalBinder(
        addItemAdditionalBinder: AddItemScreenAdditionalBinder
    ): AddItemAdditionalBinder

    @Binds
    @Singleton
    abstract fun bindAddItemPurchaseDateBinder(
        addItemPurchaseDateBinder: AddItemScreenPurchaseDateBinder
    ): AddItemPurchaseDateBinder

    @Binds
    @Singleton
    abstract fun bindAddItemExpirationDateBinder(
        addItemExpirationDateBinder: AddItemScreenExpirationDateBinder
    ): AddItemExpirationDateBinder
}
