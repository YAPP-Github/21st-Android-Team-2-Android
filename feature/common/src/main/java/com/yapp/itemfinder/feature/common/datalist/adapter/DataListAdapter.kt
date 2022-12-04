package com.yapp.itemfinder.feature.common.datalist.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yapp.itemfinder.domain.model.Category
import com.yapp.itemfinder.domain.model.CellType
import com.yapp.itemfinder.domain.model.Data

class DataListAdapter<D : Data> : ListAdapter<D, DataViewHolder<D>>(
    object : DiffUtil.ItemCallback<D>() {
        override fun areItemsTheSame(oldItem: D, newItem: D): Boolean =
            oldItem.id == newItem.id && oldItem.type == newItem.type

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: D, newItem: D): Boolean =
            oldItem === newItem
    }
) {

    override fun getItemViewType(position: Int) = getItem(position).type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<D> {
        return DataViewHolderMapper.map(parent, CellType.values()[viewType])
    }

    override fun onBindViewHolder(holder: DataViewHolder<D>, position: Int) {
        val safePosition = holder.adapterPosition
        if (safePosition != RecyclerView.NO_POSITION) {
            @Suppress("UNCHECKED_CAST")
            val model = getItem(position) as D
            with(holder) {
                bindData(model)
                bindViews(model)
                holder.itemView.setOnClickListener {
                    when (model) {
                        is Category -> model.goCategoryPage()
                    }

                }
            }
        }
    }
}
