package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.yapp.itemfinder.domain.model.AddItemTags
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.AddItemTagsBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddItemTagsViewHolder(
    val binding: AddItemTagsBinding
) : DataViewHolder<AddItemTags>(binding) {
    override fun reset() {
        binding.tagChipGroup.removeAllViews()
    }

    override fun bindData(data: AddItemTags) {
        super.bindData(data)
        val context = binding.root.context
        data.tagList.asReversed().forEach {
            val tagChip = Chip(context)
            val chipDrawable = ChipDrawable.createFromAttributes(context, null, 0, R.style.TagChip)
            binding.tagChipGroup.addView(
                tagChip.apply {
                    id = ViewCompat.generateViewId()
                    setChipDrawable(chipDrawable)
                    setTextColor(ContextCompat.getColor(context, R.color.gray_05))
                    text = it.name
                }
            )
        }
    }

    override fun bindViews(data: AddItemTags) {
        val context = binding.root.context
        val addTagChip = Chip(context)
        val chipDrawable = ChipDrawable.createFromAttributes(context, null, 0, R.style.AddTagChip)
        binding.tagChipGroup.addView(
            addTagChip.apply {
                id = ViewCompat.generateViewId()
                setChipDrawable(chipDrawable)
                text = binding.root.context.getText(R.string.add)
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setOnClickListener {
                    data.moveAddTag()
                }
            }
        )
    }

}
