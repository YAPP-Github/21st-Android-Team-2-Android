package com.yapp.itemfinder.feature.common.datalist.binder

import android.annotation.SuppressLint
import com.yapp.itemfinder.domain.model.*
import com.yapp.itemfinder.feature.common.BaseViewModel
import com.yapp.itemfinder.feature.common.datalist.binder.di.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataBindHelper @Inject constructor(
    @HomeLikeItemQualifier
    private val homeLikeItemBinder: LikeItemBinder,
    private val homeSpaceItemBinder: SpaceItemBinder,
    @HomeMySpaceUpperCellItemQualifier
    private val homeMySpaceUpperCellItemBinder: CellItemBinder,
    var lockerItemBinder: LockerItemBinder,
    var addLockerItemBinder: AddLockerItemBinder,
    var manageSpaceItemBinder: ManageSpaceItemBinder,
    var manageSpaceAddSpaceBinder: AddSpaceBinder,
    var addLockerSpaceBinder: AddLockerSpaceBinder,
    var selectSpaceBinder: SelectSpaceBinder,
    var addLockerPhotoItemBinder: AddLockerPhotoItemBinder,
    var addLockerNameBinder: AddLockerNameBinder,
    var addLockerIconBinder: AddLockerIconBinder,
    var addItemNameBinder: AddItemNameBinder,
    var addItemCategoryBinder: AddItemCategoryBinder,
    var addItemCountBinder: AddItemCountBinder,
    var addItemAdditionalBinder: AddItemAdditionalBinder,
    var addItemExpirationDateBinder: AddItemExpirationDateBinder,
    var addItemPurchaseDateBinder: AddItemPurchaseDateBinder,
    var addItemImagesBinder: AddItemImagesBinder,
    var addItemMemoBinder: AddItemMemoBinder,
    private val addItemLocationBinder: AddItemLocationBinder,
    private val addItemSelectSpaceBinder: AddItemSelectSpaceBinder,
    private val selectLockerBinder: SelectLockerBinder
) {

    @SuppressLint("CheckResult")
    fun bindList(dataList: List<Data>, viewModel: BaseViewModel) {
        dataList.forEach { data ->
            bindData(data, viewModel)
        }
    }

    private fun bindData(data: Data, viewModel: BaseViewModel) {
        when (data.type) {
            CellType.LIKE_CELL -> {
                homeLikeItemBinder.bindData(data as LikeItem, viewModel)
            }
            CellType.SPACE_CELL -> {
                homeSpaceItemBinder.bindData(data as SpaceItem, viewModel)
            }
            CellType.HOMETAB_MYSPACE_UPPER_CELL -> {
                homeMySpaceUpperCellItemBinder.bindData(data as MySpaceUpperCellItem, viewModel)
            }
            CellType.LOCKER_CELL -> {
                lockerItemBinder.bindData(data as LockerEntity, viewModel)
            }
            CellType.ADD_LOCKER_CELL -> {
                addLockerItemBinder.bindData(data as AddLocker, viewModel)
            }
            CellType.ADD_SPACE_CELL -> {
                manageSpaceAddSpaceBinder.bindData(data as AddSpace, viewModel)
            }
            CellType.MANAGE_SPACE_CELL -> {
                manageSpaceItemBinder.bindData(data as ManageSpaceEntity, viewModel)
            }
            CellType.ADD_LOCKER_SPACE_CELL -> {
                addLockerSpaceBinder.bindData(data as AddLockerSpace, viewModel)
            }
            CellType.SELECT_SPACE_CELL -> {
                selectSpaceBinder.bindData(data as SelectSpace, viewModel)
            }
            CellType.ADD_LOCKER_IMAGE_CELL -> {
                addLockerPhotoItemBinder.bindData(data as AddLockerPhoto, viewModel)
            }
            CellType.ADD_LOCKER_NAME_INPUT_CELL -> {
                addLockerNameBinder.bindData(data as AddLockerNameInput, viewModel)
            }
            CellType.LOCKER_ICONS_CELL -> {
                addLockerIconBinder.bindData(data as LockerIcons, viewModel)
            }
            CellType.ADD_ITEM_CATEGORY_CELL -> {
                addItemCategoryBinder.bindData(data as AddItemCategory, viewModel)
            }
            CellType.ADD_ITEM_COUNT_CELL -> {
                addItemCountBinder.bindData(data as AddItemCount, viewModel)
            }
            CellType.ADD_ITEM_ADDITIONAL_CELL -> {
                addItemAdditionalBinder.bindData(data as AddItemAdditional, viewModel)
            }
            CellType.ADD_ITEM_LOCATION_CELL -> {
                addItemLocationBinder.bindData(data as AddItemLocation, viewModel)
            }
            CellType.ADD_ITEM_EXPIRATION_DATE_CELL -> {
                addItemExpirationDateBinder.bindData(data as AddItemExpirationDate, viewModel)
            }
            CellType.ADD_ITEM_PURCHASE_DATE_CELL -> {
                addItemPurchaseDateBinder.bindData(data as AddItemPurchaseDate, viewModel)
            }
            CellType.ADD_ITEM_NAME_CELL -> {
                addItemNameBinder.bindData(data as AddItemName, viewModel)
            }
            CellType.ADD_ITEM_MEMO_CELL -> {
                addItemMemoBinder.bindDate(data as AddItemMemo, viewModel)
            }
            CellType.ADD_ITEM_IMAGES_CELL -> addItemImagesBinder.bindData(
                data as AddItemImages, viewModel
            )
            CellType.ADD_ITEM_SELECT_SPACE_CELL -> addItemSelectSpaceBinder.bindData(
                data as AddItemSelectSpaceEntity, viewModel
            )
            CellType.SELECT_LOCKER_CELL -> selectLockerBinder.bindData(
                data as SelectLockerEntity, viewModel
            )
            else -> {}
        }
    }
}
