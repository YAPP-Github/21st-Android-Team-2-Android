package com.yapp.itemfinder.space.binder.di

import com.yapp.itemfinder.feature.common.datalist.binder.*
import com.yapp.itemfinder.feature.common.datalist.binder.di.*
import com.yapp.itemfinder.space.binder.*
import com.yapp.itemfinder.space.binder.additem.AddItemScreenAddItemLocationBinder
import com.yapp.itemfinder.space.binder.additem.selectlocker.AddItemSelectLockerBinder
import com.yapp.itemfinder.space.binder.additem.selectspace.AddItemSelectSpaceScreenAddItemSelectSpaceBinder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AddItemDataBinderModule {
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
    abstract fun bindAddItemLocationBinder(
        addItemLocationBinder: AddItemScreenAddItemLocationBinder
    ): AddItemLocationBinder

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

    @Binds
    @Singleton
    abstract fun bindAddItemNameBinder(
        addItemNameBinder: AddItemScreenNameBinder
    ): AddItemNameBinder

    @Binds
    @Singleton
    abstract fun bindAddItemMemoBinder(
        addItemMemoBinder: AddItemScreenMemoBinder
    ): AddItemMemoBinder


    @Binds
    @Singleton
    abstract fun bindAddItemSelectSpaceBinder(
        addItemSelectSpaceBinder: AddItemSelectSpaceScreenAddItemSelectSpaceBinder
    ): AddItemSelectSpaceBinder

    @Binds
    @Singleton
    abstract fun bindAddItemSelectLockerBinder(
        addItemSelectLockerBinder: AddItemSelectLockerBinder
    ): SelectLockerBinder


    @Binds
    @Singleton
    abstract fun bindAddIteImagesBinder(
        addItemMemoBinder: AddItemScreenImagesBinder
    ): AddItemImagesBinder

    @Binds
    @Singleton
    abstract fun bindAddItemMarkerMapBinder(
        addItemMarkerMapBinder: AddItemScreenMarkerMapBinder
    ): AddItemMarkerMapBinder

}
