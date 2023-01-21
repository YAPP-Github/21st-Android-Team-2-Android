package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.AddItemInfoRequired
import com.yapp.itemfinder.domain.model.ItemCategorySelection
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.AddItemInfoRequiredBinding

class AddItemInfoRequiredViewHolder(
    val binding: AddItemInfoRequiredBinding
) : DataViewHolder<AddItemInfoRequired>(binding) {
    override fun reset() {
        return
    }

    override fun bindViews(data: AddItemInfoRequired) {
        binding.itemCategoryButton.text = data.category
        binding.itemCategoryButton.setOnClickListener { data.selectCategory() }
        when (data.category) {
            ItemCategorySelection.DEFAULT.label -> {
                binding.itemCategoryButton.apply {
                    background = itemView.context.getDrawable(R.drawable.bg_category_default_button)
                    setTextColor(itemView.context.getColor(R.color.gray_03))
                }
            }
            else -> {
                binding.itemCategoryButton.apply {
                    background =
                        itemView.context.getDrawable(R.drawable.bg_category_selected_button)
                    setTextColor(itemView.context.getColor(R.color.gray_05))
                }
            }
        }
        binding.countTextView.text = data.count.toString()
    }

}
