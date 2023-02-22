package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.feature.common.databinding.ViewholderTagBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class TagViewHolder(
    val binding: ViewholderTagBinding
) : DataViewHolder<Tag>(binding) {

    override fun reset() = Unit

    override fun bindData(data: Tag) {
        super.bindData(data)
        binding.tag.text = data.name
    }

    override fun bindViews(data: Tag) {
        binding.tag.setOnClickListener {
            data.selectTag()
        }
    }

}
