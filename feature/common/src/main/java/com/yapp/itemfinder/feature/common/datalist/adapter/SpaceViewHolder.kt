package com.yapp.itemfinder.feature.common.datalist.adapter

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.SpaceItemBinding
import com.yapp.itemfinder.feature.common.extension.visible

class SpaceViewHolder(
    val binding: SpaceItemBinding
) : DataViewHolder<SpaceItem>(binding) {

    override fun bindData(data: SpaceItem) {
        super.bindData(data)
        val spaceName = data.name
        val lockersInSpace = data.lockerList
        with(binding) {
            spaceItemName.text = spaceName

            val imageViews = listOf(spaceFirstImageView, spaceSecondImageView, spaceThirdImageView, spaceFourthImageView)
            val frames = listOf(spaceFirstFrame, spaceSecondFrame, spaceThirdFrame, spaceFourthFrame)

            for (i in lockersInSpace.indices){

                val frame = frames[i]
                val imageView = imageViews[i]

                when(i){
                    0,1,2 -> {
                        Glide.with(itemView).load(R.drawable.box).into(imageView)
                    }
                    3 ->{
                        if (lockersInSpace.size > 4){
                            spaceFourthTextView.text = "+${lockersInSpace.size-3}"
                            frame.setCardBackgroundColor(frame.context.getColor(R.color.brown_03))
                        }else{
                            Glide.with(itemView).load(R.drawable.box).into(imageView)
                        }
                    }
                }
                frame.visible()
                if (i == 3) break
            }
        }
    }

    override fun bindViews(data: SpaceItem) {
        binding.root.setOnClickListener { data.goSpaceDetailPage() }
    }

    override fun reset() {

    }
}
