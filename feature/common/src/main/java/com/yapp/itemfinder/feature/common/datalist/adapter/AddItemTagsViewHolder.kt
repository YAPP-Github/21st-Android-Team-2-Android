package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.AddItemTags
import com.yapp.itemfinder.feature.common.databinding.AddItemTagsBinding

class AddItemTagsViewHolder(
    val binding: AddItemTagsBinding
) : DataViewHolder<AddItemTags>(binding){
    override fun reset() {
        return
    }

    override fun bindViews(data: AddItemTags) {
        data.tagList.forEach {  }
    }

}
