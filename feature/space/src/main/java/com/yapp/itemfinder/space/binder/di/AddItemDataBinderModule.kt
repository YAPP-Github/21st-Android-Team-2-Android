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
    @AddItemCategoryQualifier
    abstract fun bindAddItemCategoryBinder(
        addItemCategoryBinder: AddItemScreenCategoryBinder
    ): AddItemCategoryBinder

    @Binds
    @Singleton
    @EditItemCategoryQualifier
    abstract fun bindEditItemCategoryBinder(
        editItemCategoryBinder: EditItemScreenCategoryBinder
    ): AddItemCategoryBinder

    @Binds
    @Singleton
    @AddItemCountQualifier
    abstract fun bindAddItemCountBinder(
        addItemCountBinder: AddItemScreenCountBinder
    ): AddItemCountBinder

    @Binds
    @Singleton
    @EditItemCountQualifier
    abstract fun bindEditItemCountBinder(
        editItemCountBinder: EditItemScreenCountBinder
    ): AddItemCountBinder

    @Binds
    @Singleton
    @AddItemAdditionalQualifier
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
    @EditItemAdditionalQualifier
    abstract fun bindEditItemAdditionalBinder(
        editItemAdditionalBinder: EditItemScreenAdditionalBinder
    ): AddItemAdditionalBinder

    @Binds
    @Singleton
    @AddItemPurchaseDateQualifier
    abstract fun bindAddItemPurchaseDateBinder(
        addItemPurchaseDateBinder: AddItemScreenPurchaseDateBinder
    ): AddItemPurchaseDateBinder

    @Binds
    @Singleton
    @EditItemPurchaseDateQualifier
    abstract fun bindEditItemPurchaseDateBinder(
        editItemPurchaseDateBinder: EditItemScreenPurchaseDateBinder
    ): AddItemPurchaseDateBinder

    @Binds
    @Singleton
    @AddItemExpirationDateQualifier
    abstract fun bindAddItemExpirationDateBinder(
        addItemExpirationDateBinder: AddItemScreenExpirationDateBinder
    ): AddItemExpirationDateBinder

    @Binds
    @Singleton
    @EditItemExpirationDateQualifier
    abstract fun bindEditItemExpirationDateBinder(
        editItemExpirationDateBinder: EditItemScreenExpirationDateBinder
    ): AddItemExpirationDateBinder

    @Binds
    @Singleton
    @AddItemNameQualifier
    abstract fun bindAddItemNameBinder(
        addItemNameBinder: AddItemScreenNameBinder
    ): AddItemNameBinder

    @Binds
    @Singleton
    @EditItemNameQualifier
    abstract fun bindEditItemNameBinder(
        editItemBinder: EditItemScreenNameBinder
    ): AddItemNameBinder

    @Binds
    @Singleton
    @AddItemMemoQualifier
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
    @EditItemMemoQualifier
    abstract fun bindEditItemMemoBinder(
        editItemMemoBinder: EditItemScreenMemoBinder
    ): AddItemMemoBinder

}
