package com.yapp.itemfinder.feature.common.datalist.adapter

import com.bumptech.glide.Glide
import com.yapp.itemfinder.domain.model.Thing
import com.yapp.itemfinder.feature.common.databinding.ThingSimpleItemBinding
import com.yapp.itemfinder.feature.common.extension.gone

class ThingSimpleViewHolder(
    val binding: ThingSimpleItemBinding
) : DataViewHolder<Thing>(binding) {
    override fun reset() {
    }

    override fun bindData(data: Thing) {
        super.bindData(data)
        with(binding) {
            Glide.with(root.context).load(data.imageUrl).into(imageView)
            nameTextView.text = data.name
            thingNumTextView.text = "3개 (dummy)"

            data.expirationDate?.let {
                expireDateTextView.text = it
            } ?: kotlin.run {
                expireDateTextView.gone()
            }

            data.thingCategory?.let {
                thingCategory.text = it.name
            } ?: kotlin.run {
                thingCategory.gone()
            }

        }
    }

    override fun bindViews(data: Thing) {
        binding.root.setOnClickListener {
            return@setOnClickListener
        }
    }
}
