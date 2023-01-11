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

    private enum class State {
        NORMAL, OVER
    }

    lateinit var spaceName: String
    lateinit var lockers: List<Locker>
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
        if (lockers.size > 4)
            state = State.OVER

        with(binding) {
            spaceItemName.text = spaceName

            lockers.indices.forEach { idx ->
                val frame = frames[idx]
                val imageView = imageViews[idx]
                frame.visible()
                when (idx) {
                    0, 1, 2 -> {
                        Glide.with(itemView).load(com.yapp.itemfinder.domain.R.drawable.box)
                            .into(imageView)
                    }
                    3 -> {
                        when (state) {
                            State.NORMAL -> {
                                Glide.with(itemView).load(com.yapp.itemfinder.domain.R.drawable.box)
                                    .into(imageView)
                            }
                            State.OVER -> {
                                spaceFourthTextView.text = "+${lockers.size - 3}"
                                frame.setCardBackgroundColor(frame.context.getColor(R.color.brown_03))
                            }

                        }
                        return
                    }
                }
            }
        }
    }

    override fun bindViews(data: SpaceItem) {

        binding.root.setOnClickListener { data.goSpaceDetailPage() }
        lockers.forEachIndexed { idx, locker ->
            when (idx) {
                0, 1, 2 -> frames[idx].setOnClickListener { data.lockerDetailHandler(locker) }
                3 -> when (state) {
                    State.NORMAL -> frames[idx].setOnClickListener { data.lockerDetailHandler(locker) }
                    else -> Unit

                }
                4 -> return
            }
        }
    }

    override fun reset() {
        frames.forEach { it.invisible() }
    }
}
