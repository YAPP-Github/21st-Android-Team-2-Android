package com.yapp.itemfinder.feature.common.datalist.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yapp.itemfinder.feature.common.datalist.model.Data

abstract class DataViewHolder<D: Data>(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun reset()

    open fun bindData(data: D) {
        reset()
    }

    abstract fun bindViews(data: D)

}
