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
            CellType.EMPTY_CELL -> EmptyCellViewHolder(EmptyCellItemBinding.inflate(inflater,parent,false))
            CellType.CATEGORY_CELL -> CategoryViewHolder(ViewholderStorageBinding.inflate(inflater,parent,false))
            CellType.LIKE_CELL -> LikeViewHolder(LikeItemBinding.inflate(inflater, parent, false))
            CellType.SPACE_CELL -> SpaceViewHolder(SpaceItemBinding.inflate(inflater,parent,false))
            CellType.LOCKER_CELL -> LockerViewHolder(LockerListItemBinding.inflate(inflater, parent, false))
            CellType.ADD_LOCKER_CELL -> AddLockerViewHolder(AddLockerBinding.inflate(inflater, parent, false))
            CellType.HOMETAB_MYSPACE_UPPER_CELL -> HomeTabMySpaceUpperCellViewHolder(HometabMyspaceUpperCellItemBinding.inflate(inflater, parent, false))
            CellType.ADD_SPACE_CELL -> AddSpaceViewHolder(AddSpaceBinding.inflate(inflater, parent, false))
            CellType.MANAGE_SPACE_CELL -> ManageSpaceViewHolder(ManageSpaceItemBinding.inflate(inflater, parent, false))
            CellType.ADD_LOCKER_NAME_INPUT_CELL -> AddLockerNameInputViewHolder(
                AddLockerNameInputBinding.inflate(inflater, parent, false))
            CellType.ADD_LOCKER_SPACE_CELL -> AddLockerSpaceViewHolder(AddLockerSpaceBinding.inflate(inflater, parent, false))
            CellType.LOCKER_ICONS_CELL -> LockerIconsViewHolder(LockerIconsBinding.inflate(inflater, parent, false))
            CellType.ADD_LOCKER_IMAGE_CELL -> AddLockerPhotoViewHolder(AddLockerPhotoBinding.inflate(inflater, parent, false))
            CellType.SELECT_SPACE_CELL -> SelectSpaceViewHolder(SelectSpaceBinding.inflate(inflater, parent, false))
            CellType.ITEM_SIMPLE_CELL ->  ItemSimpleViewHolder(ItemSimpleItemBinding.inflate(inflater,parent,false))
        }

        return viewHolder as DataViewHolder<D>
    }

}
