package com.yapp.itemfinder.feature.common.views.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.allViews
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.LayoutItemsMarkerMapBinding

class ItemsMarkerMapView
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {

    init {
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private val binding: LayoutItemsMarkerMapBinding =
        LayoutItemsMarkerMapBinding.inflate(LayoutInflater.from(context), this, true)

    fun fetchItems(items: List<Item>, targetItem: Item?) = with(binding) {
        allViews.forEach {
            if (it is AppCompatImageView && it.id != R.id.markerBackgroundImageView) {
                removeView(it)
            }
        }
        val deselectedItems = items.filter { it.id != targetItem?.id }
        deselectedItems.forEach {
            val itemMarkerView = ItemMarkerView(context)
            addView(itemMarkerView)
            itemMarkerView.item = it
            itemMarkerView.selected = false
            itemMarkerView.position = it.position
        }
        targetItem?.let {
            val itemMarkerView = ItemMarkerView(context)
            addView(itemMarkerView)
            itemMarkerView.item = it
            itemMarkerView.selected = true
            itemMarkerView.position = it.position
        }
    }

}
