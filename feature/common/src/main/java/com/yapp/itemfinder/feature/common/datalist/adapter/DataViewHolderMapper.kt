package com.yapp.itemfinder.feature.common.datalist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.yapp.itemfinder.feature.common.datalist.model.CellType
import com.yapp.itemfinder.feature.common.datalist.model.Data

object DataViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <D: Data> map(
        parent: ViewGroup,
        type: CellType,
    ): DataViewHolder<D> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> null // TODO: Implementation for nonnull
        }

        return viewHolder as DataViewHolder<D>
    }

}
