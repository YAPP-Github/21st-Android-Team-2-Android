package com.yapp.itemfinder.feature.common.datalist.adapter

import com.bumptech.glide.Glide
import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.SpaceItemBinding
import com.yapp.itemfinder.feature.common.extension.invisible
import com.yapp.itemfinder.feature.common.extension.visible

class SpaceViewHolder(
    val binding: SpaceItemBinding
) : DataViewHolder<SpaceItem>(binding) {
    lateinit var spaceName: String
    lateinit var lockersInSpace: List<Locker>
    private val imageViews = listOf(
        binding.spaceFirstImageView,
        binding.spaceSecondImageView,
        binding.spaceThirdImageView,
        binding.spaceFourthImageView
    )
    private val frames = listOf(
        binding.spaceFirstFrame,
        binding.spaceSecondFrame,
        binding.spaceThirdFrame,
        binding.spaceFourthFrame
    )

    override fun bindData(data: SpaceItem) {
        super.bindData(data)
        spaceName = data.name
        lockersInSpace = data.lockerList
        with(binding) {
            spaceItemName.text = spaceName

            lockersInSpace.indices.forEach { idx ->
                val frame = frames[idx]
                val imageView = imageViews[idx]
                frame.visible()
                when (idx) {
                    0, 1, 2 -> {
                        Glide.with(itemView).load(com.yapp.itemfinder.domain.R.drawable.box).into(imageView)
                    }
                    3 -> {
                        if (lockersInSpace.size > 4) {
                            spaceFourthTextView.text = "+${lockersInSpace.size - 3}"
                            frame.setCardBackgroundColor(frame.context.getColor(R.color.brown_03))
                        } else {
                            Glide.with(itemView).load(com.yapp.itemfinder.domain.R.drawable.box).into(imageView)
                        }
                        return
                    }
                }
            }
        }
    }

    override fun bindViews(data: SpaceItem) {
        binding.root.setOnClickListener { data.goSpaceDetailPage() }
    }

    override fun reset() {
        frames.forEach { it.invisible() }
    }
}
