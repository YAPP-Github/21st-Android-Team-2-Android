package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.bumptech.glide.Glide
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.SpaceItemBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder
import com.yapp.itemfinder.feature.common.extension.invisible
import com.yapp.itemfinder.feature.common.extension.toDrawable
import com.yapp.itemfinder.feature.common.extension.visible

class SpaceViewHolder(
    val binding: SpaceItemBinding
) : DataViewHolder<SpaceItem>(binding) {
    private val context = binding.root.context

    private enum class State {
        NORMAL, OVER
    }

    private lateinit var spaceName: String
    private lateinit var lockers: List<LockerEntity>
    private var state: State = State.NORMAL
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
        lockers = data.lockerList
        with(binding) {
            spaceItemName.text = spaceName

            lockers.indices.forEach { idx ->
                val frame = frames[idx]
                val imageView = imageViews[idx]
                frame.visible()
                when (idx) {
                    0, 1, 2 -> {
                        Glide.with(itemView).load(lockers[idx].icon.toDrawable(context)).into(imageView)
                    }
                    3 -> {
                        if (data.lockerCount > 4) {
                            state = State.OVER
                            spaceFourthTextView.text = "+${lockers.size - 3}"
                            frame.setCardBackgroundColor(frame.context.getColor(R.color.brown_03))
                        } else {
                            val context = binding.root.context
                            Glide.with(itemView).load(lockers[idx].icon.toDrawable(context)).into(imageView)
                        }
                        return
                    }
                }
            }
        }
    }

    override fun bindViews(data: SpaceItem) {
        binding.root.setOnClickListener { data.moveSpaceDetailPage() }
        lockers.forEachIndexed { idx, locker ->
            when (idx) {
                0, 1, 2 -> frames[idx].setOnClickListener { data.moveLockerDetailHandler(locker) }
                3 -> when (state) {
                    State.NORMAL -> frames[idx].setOnClickListener { data.moveLockerDetailHandler(locker) }
                    State.OVER -> frames[idx].setOnClickListener { data.moveSpaceDetailPage() }

                }
                4 -> return
            }
        }
    }

    override fun reset() {
        frames.forEach { it.invisible() }
    }
}
