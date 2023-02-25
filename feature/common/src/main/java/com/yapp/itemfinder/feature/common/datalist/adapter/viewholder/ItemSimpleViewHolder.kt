package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.ItemCategory
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.ItemSimpleItemBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder
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

            data.representativeImage?.let {
                Glide.with(context).load(it).into(imageView)
            } ?: kotlin.run {
                data.itemCategory?.let {
                    when(it){
                        ItemCategory.LIFE ->{
                            Glide.with(context).load(R.drawable.ic_item_life).into(imageView)
                        }
                        ItemCategory.FOOD -> {
                            Glide.with(context).load(R.drawable.ic_item_food).into(imageView)
                        }
                        ItemCategory.FASHION ->{
                            Glide.with(context).load(R.drawable.ic_item_fashion).into(imageView)
                        }
                        ItemCategory.NONE -> {

                        }
                    }
                }

            }


            nameTextView.text = data.name

            if (data.count in 2 .. 99){
                itemNumTextView.text = context.getString(R.string.count, data.count)
            }else{
                itemNumTextView.gone()
            }


            data.expirationDate?.let {
                expireDateTextView.text = root.context.getString(R.string.until_expired_date, it)
            } ?: kotlin.run {
                expireDateTextView.gone()
            }

            var currentSize = 0
            data.itemCategory?.let {
                val labelText = root.context.getString(it.labelResId)
                thingCategory.text = labelText
                currentSize += labelText.length
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
                        text =
                            if (isOverflow) "···"
                            else tag.name

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

            data.itemFocusHandler = { isFocused ->
                binding.itemFocusView.isVisible = isFocused
            }

        }
    }

    override fun bindViews(data: Item) {
        binding.root.setOnClickListener {
            data.moveItemDetail()
            return@setOnClickListener
        }
    }

    companion object {
        private const val CONDITION_OVERFLOW = 11
    }
}
