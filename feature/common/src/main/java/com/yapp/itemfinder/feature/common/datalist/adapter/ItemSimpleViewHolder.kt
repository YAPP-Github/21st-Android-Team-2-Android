package com.yapp.itemfinder.feature.common.datalist.adapter

import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import com.bumptech.glide.Glide
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.ItemSimpleItemBinding
import com.yapp.itemfinder.feature.common.extension.dpToPx
import com.yapp.itemfinder.feature.common.extension.gone

class ItemSimpleViewHolder(
    val binding: ItemSimpleItemBinding
) : DataViewHolder<Item>(binding) {

    private val context = binding.root.context

    override fun reset() {
        binding.tagsLayout.removeAllViews()
    }

    override fun bindData(data: Item) {
        super.bindData(data)
        with(binding) {
            Glide.with(root.context).load(data.imageUrl).into(imageView)
            nameTextView.text = data.name

            if (data.count in 2 .. 99){
                itemNumTextView.text = context.getString(R.string.count, data.count)
            }else{
                itemNumTextView.gone()
            }


            data.expirationDate?.let {
                expireDateTextView.text = it
            } ?: kotlin.run {
                expireDateTextView.gone()
            }

            var currentSize = 0
            data.itemCategory?.let {
                thingCategory.text = it.label
                currentSize += it.label.length
            } ?: kotlin.run {
                thingCategory.gone()
            }


            data.tags?.let {
                for (tag in it) {
                    currentSize += tag.name.length
                    val isOverflow = currentSize >= CONDITION_OVERFLOW

                    val frameLayOut = FrameLayout(
                        ContextThemeWrapper(
                            context,
                            if (isOverflow) R.style.ChipFrameStyle_Overflow else R.style.ChipFrameStyle_Tag
                        )
                    )
                    val textView = TextView(
                        ContextThemeWrapper(
                            context,
                            if (isOverflow) R.style.ChipTextViewStyle_Overflow else R.style.ChipTextViewStyle_Tag
                        )
                    ).apply {
                        if (isOverflow)
                            text = "···"
                        else
                            text = tag.name

                    }
                    frameLayOut.addView(textView)
                    tagsLayout.addView(frameLayOut)
                    frameLayOut.layoutParams = LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                    ).apply { setMargins(4.dpToPx(context), 0, 0, 0) }

                    if (isOverflow) break
                }

            }

        }
    }

    override fun bindViews(data: Item) {
        binding.root.setOnClickListener {
            return@setOnClickListener
        }
    }

    companion object {
        private const val CONDITION_OVERFLOW = 11
    }
}
