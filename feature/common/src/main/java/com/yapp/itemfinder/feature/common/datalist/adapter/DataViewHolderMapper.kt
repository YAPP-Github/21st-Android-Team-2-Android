package com.yapp.itemfinder.feature.common.datalist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yapp.itemfinder.domain.model.CellType
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.databinding.*

object DataViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <D: Data> map(
        parent: ViewGroup,
        type: CellType,
    ): DataViewHolder<D> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.ADD_ITEM_MARKER_MAP_CELL, CellType.EMPTY_CELL -> EmptyCellViewHolder(EmptyCellItemBinding.inflate(inflater,parent,false))
            CellType.CATEGORY_CELL -> CategoryViewHolder(ViewholderStorageBinding.inflate(inflater,parent,false))
            CellType.LIKE_CELL -> LikeViewHolder(LikeItemBinding.inflate(inflater, parent, false))
            CellType.SPACE_CELL -> SpaceViewHolder(SpaceItemBinding.inflate(inflater,parent,false))
            CellType.LOCKER_CELL -> LockerViewHolder(LockerListItemBinding.inflate(inflater, parent, false))
            CellType.ADD_LOCKER_CELL -> AddLockerViewHolder(AddLockerBinding.inflate(inflater, parent, false))
            CellType.HOMETAB_MYSPACE_UPPER_CELL -> HomeTabMySpaceUpperCellViewHolder(HometabMyspaceUpperCellItemBinding.inflate(inflater, parent, false))
            CellType.ADD_SPACE_CELL -> AddSpaceViewHolder(AddSpaceBinding.inflate(inflater, parent, false))
            CellType.MANAGE_SPACE_CELL -> ManageSpaceViewHolder(ManageSpaceItemBinding.inflate(inflater, parent, false))
            CellType.ADD_LOCKER_NAME_INPUT_CELL -> AddLockerNameInputViewHolder(AddLockerNameInputBinding.inflate(inflater, parent, false))
            CellType.ADD_LOCKER_SPACE_CELL -> AddLockerSpaceViewHolder(AddLockerSpaceBinding.inflate(inflater, parent, false))
            CellType.LOCKER_ICONS_CELL -> LockerIconsViewHolder(LockerIconsBinding.inflate(inflater, parent, false))
            CellType.ADD_LOCKER_IMAGE_CELL -> AddLockerPhotoViewHolder(AddLockerPhotoBinding.inflate(inflater, parent, false))
            CellType.SELECT_SPACE_CELL -> SelectSpaceViewHolder(SelectSpaceBinding.inflate(inflater, parent, false))
            CellType.ITEM_SIMPLE_CELL ->  ItemSimpleViewHolder(ItemSimpleItemBinding.inflate(inflater,parent,false))
            CellType.ADD_ITEM_NAME_CELL -> AddItemNameViewHolder(AddItemNameBinding.inflate(inflater, parent, false))
            CellType.ADD_ITEM_CATEGORY_CELL -> AddItemCategoryViewHolder(AddItemCategoryBinding.inflate(inflater, parent, false))
            CellType.ADD_ITEM_LOCATION_CELL -> AddItemLocationViewHolder(AddItemLocationBinding.inflate(inflater, parent, false))
            CellType.ADD_ITEM_COUNT_CELL -> AddItemCountViewHolder(AddItemCountBinding.inflate(inflater, parent, false))
            CellType.ADD_ITEM_TAGS_CELL -> AddItemTagsViewHolder(AddItemTagsBinding.inflate(inflater, parent, false))
            CellType.ADD_ITEM_ADDITIONAL_CELL -> AddItemAdditionalViewHolder(
                AddItemAdditionalBinding.inflate(inflater, parent, false))
            CellType.ADD_ITEM_MEMO_CELL -> AddItemMemoViewHolder(AddItemMemoBinding.inflate(inflater, parent, false))
            CellType.ADD_ITEM_EXPIRATION_DATE_CELL -> AddItemExpirationDateViewHolder(
                AddItemExpirationDateBinding.inflate(inflater, parent, false))
            CellType.ADD_ITEM_PURCHASE_DATE_CELL ->
                AddItemPurchaseDateViewHolder(AddItemPurchaseDateBinding.inflate(inflater, parent, false))
            CellType.ADD_ITEM_SELECT_SPACE_CELL ->
                AddItemSelectSpaceViewHolder(ViewholderAddItemSelectSpaceBinding.inflate(inflater, parent, false))
            CellType.SELECT_LOCKER_CELL ->
                SelectLockerViewHolder(ViewholderSelectLockerBinding.inflate(inflater, parent, false))
        }

        return viewHolder as DataViewHolder<D>
    }

}
