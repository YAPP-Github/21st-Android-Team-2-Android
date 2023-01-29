package com.yapp.itemfinder.feature.common.datalist.adapter

import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.yapp.itemfinder.domain.model.AddItemTags
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.AddItemTagsBinding

class AddItemTagsViewHolder(
    val binding: AddItemTagsBinding
) : DataViewHolder<AddItemTags>(binding){
    override fun reset() {
        return
    }

    override fun bindData(data: AddItemTags) {
        super.bindData(data)
        data.tagList.asReversed().forEach {
            val tv = TextView(binding.root.context)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 8, 0)
            tv.apply {
                text = it.name
                setTextColor(
                    ResourcesCompat.getColor(
                        binding.root.resources, R.color.gray_05, null
                    )
                )
                background = ResourcesCompat.getDrawable(
                    binding.root.resources,
                    R.drawable.bg_chip_tag,
                    null
                )
                setTextAppearance(R.style.ItemFinderTypography)
                layoutParams = params
            }
            binding.itemTagsLayout.addView(tv, 0)
        }
    }

    override fun bindViews(data: AddItemTags) {
        return
    }

}
