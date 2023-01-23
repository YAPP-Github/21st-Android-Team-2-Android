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

    private val itemMarkerViews = mutableListOf<ItemMarkerView>()
    fun fetchItems(items: List<Item>) = with(binding) {
        allViews.filterIsInstance<ItemMarkerView>().forEach {
            removeView(it)
        }

        items.forEach { item ->
            val itemMarkerView = ItemMarkerView(context)
            addView(itemMarkerView)
            itemMarkerView.id = item.id.toInt()
            itemMarkerView.item = item
            itemMarkerView.position = item.position
            itemMarkerViews.add(itemMarkerView)
        }
    }

    fun applyFocusMarker(item: Item) {
        itemMarkerViews.forEach {
            it.isFocused = it.id == item.id.toInt()
        }
    }

}
