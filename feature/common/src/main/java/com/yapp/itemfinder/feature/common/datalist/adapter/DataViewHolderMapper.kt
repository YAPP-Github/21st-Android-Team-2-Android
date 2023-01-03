package com.yapp.itemfinder.feature.common.datalist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yapp.itemfinder.domain.model.CellType
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.databinding.LikeItemBinding
import com.yapp.itemfinder.feature.common.databinding.SpaceItemBinding
import com.yapp.itemfinder.feature.common.databinding.ViewholderStorageBinding

object DataViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <D: Data> map(
        parent: ViewGroup,
        type: CellType,
    ): DataViewHolder<D> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> null
            CellType.CATEGORY_CELL -> CategoryViewHolder(ViewholderStorageBinding.inflate(inflater,parent,false))
            CellType.LIKE_CELL -> LikeViewHolder(LikeItemBinding.inflate(inflater, parent, false))
            CellType.SPACE_CELL -> SpaceViewHolder(SpaceItemBinding.inflate(inflater,parent,false))
        }

        return viewHolder as DataViewHolder<D>
    }

}
