package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddItemCategory
import com.yapp.itemfinder.domain.model.ItemCategorySelection
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.AddItemCategoryBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddItemCategoryViewHolder(
    val binding: AddItemCategoryBinding
) : DataViewHolder<AddItemCategory>(binding) {
    override fun reset() {
        return
    }

    override fun bindData(data: AddItemCategory) {
        super.bindData(data)
        binding.itemCategoryButton.text = data.category.label
    }

    override fun bindViews(data: AddItemCategory) {
        binding.itemCategoryButton.setOnClickListener { data.selectCategory() }
        when (data.category) {
            ItemCategorySelection.DEFAULT -> {
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
    }

}
