package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.SelectLockerEntity
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.ViewholderSelectLockerBinding
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.toDrawable
import com.yapp.itemfinder.feature.common.extension.visible

class SelectLockerViewHolder(
    val binding: ViewholderSelectLockerBinding
) : DataViewHolder<SelectLockerEntity>(binding) {

    override fun reset() {
        binding.lockerItemTextView.setTextColor(itemView.context.getColor(R.color.gray_06))
        binding.checkImage.gone()
    }

    override fun bindData(data: SelectLockerEntity) {
        super.bindData(data)
        binding.lockerItemTextView.text = data.name
        binding.lockerItemImageView.setImageDrawable(data.icon.toDrawable(binding.root.context))

        if (data.isSelected) {
            binding.lockerItemTextView.setTextColor(itemView.context.getColor(R.color.orange))
            binding.checkImage.visible()
            binding.checkImage.setColorFilter(itemView.context.getColor(R.color.orange))
        }
    }

    override fun bindViews(data: SelectLockerEntity) {
        binding.root.setOnClickListener {
            data.selectLocker()
        }
    }
}
