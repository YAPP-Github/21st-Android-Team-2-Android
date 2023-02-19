package com.yapp.itemfinder.feature.common.views.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.allViews
import com.bumptech.glide.Glide
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.LockerEntity
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
    fun fetchItems(lockerEntity: LockerEntity, items: List<Item>) = with(binding) {
        Glide.with(context)
            .load(lockerEntity.imageUrl)
            .into(binding.markerBackgroundImageView)

        if (itemMarkerViews.size == 1) {
            allViews.filterIsInstance<ItemMarkerView>().forEach { removeView(it) }
        } else {
            removeAllMarkers()
        }

        items.forEach { item ->
            addView(
                ItemMarkerView(context).apply {
                    this.id = item.id.toInt()
                    this.item = item
                    this.position = item.position
                    itemMarkerViews.add(this)
                }
            )
        }

        items.forEach { item ->
            applyFocusMarker(item)
        }
    }

    fun applyFocusMarker(item: Item) {
        itemMarkerViews.forEach {
            it.isFocused = it.id == item.id.toInt()
            if (it.isFocused == true) {
                it.bringToFront()
            }
        }
    }

    fun setBackgroundImage(url: String) {
        Glide.with(context).load(url).into(binding.markerBackgroundImageView)
    }

    fun addMarkerAndBringToFront(item: Item) {
        removeAllMarkers()

        addView(
            ItemMarkerView(context).apply {
                this.id = item.id.toInt()
                this.item = item
                this.position = item.position
                isFocused = true
                itemMarkerViews.add(this)
            }
        )

        postDelayed({
            itemMarkerViews.forEach { it.bringToFront() }
        }, 100)
    }

    fun createFocusMarker(x: Float, y: Float): Item {
        val mapHorizontalGap = (binding.markerMapContainer.measuredWidth - binding.markerBackgroundImageView.measuredWidth) / 2

        val mapStartX: Int = mapHorizontalGap
        val mapEndX: Int = binding.markerMapContainer.measuredWidth - mapHorizontalGap
        val mapStartY = 0
        val mapEndY: Int = binding.markerMapContainer.measuredHeight

        val xPosition: Int = when {
            x < mapStartX -> mapStartX
            x > mapEndX -> mapEndX
            else -> x.toInt()
        }
        val yPosition: Int = when {
            y < mapStartY -> mapStartY
            y > mapEndY -> mapEndY
            else -> y.toInt()
        }

        val xRatioPosition = (xPosition - mapStartX) / binding.markerBackgroundImageView.measuredWidth.toFloat() * 100
        val yRatioPosition = yPosition / binding.markerBackgroundImageView.measuredHeight.toFloat() * 100

        val position = Item.Position(xRatioPosition, yRatioPosition)
        return Item.createEmptyItem(position)
    }

    fun getImageHeight() = binding.markerBackgroundImageView.measuredHeight

    private fun removeAllMarkers() {
        val markerViews = itemMarkerViews.toList()
        markerViews.forEach { removeView(it) }
        itemMarkerViews.clear()
    }

}
