package com.yapp.itemfinder.feature.common.views.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.feature.common.databinding.LayoutItemMarkerBinding
import com.yapp.itemfinder.feature.common.extension.*

class ItemMarkerView
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {

    private val binding: LayoutItemMarkerBinding =
        LayoutItemMarkerBinding.inflate(LayoutInflater.from(context), this)

    init {
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
    }

    var item: Item? = null
        set(value) {
            field = value
            value?.itemCategory?.iconResId?.let {
                binding.markerIconImageView.setImageDrawable(
                    ContextCompat.getDrawable(context, it)
                )
                binding.selectedMarkerIconImageView.setImageDrawable(
                    ContextCompat.getDrawable(context, it)
                )
            }
        }

    var isFocused: Boolean? = null
        set(value) {
            field = value
            value?.let {
                if (it) {
                    showSelectedMarker(true)
                } else {
                    showSelectedMarker(false)
                }
            }
        }

    private fun showSelectedMarker(isShow: Boolean) {
        if (isShow) {
            binding.markerIconImageView.invisible()
            binding.selectedItemGroup.visible()
        } else {
            binding.markerIconImageView.visible()
            binding.selectedItemGroup.invisible()
        }
    }

    var position: Item.Position? = null
        set(value) {
            field = value
            val xPercentage = (position?.x ?: 0f) / 100f
            val yPercentage = (position?.y ?: 0f) / 100f

            var isDrawn = false
            doOnGlobalLayout {
                if (isDrawn) return@doOnGlobalLayout
                val parentView = (parent as ConstraintLayout)
                val xMargin = parentView.measuredWidth * if (xPercentage > 0.95) 0.95f else xPercentage
                val yMargin = parentView.measuredHeight * if (yPercentage > 0.95) 0.95f else yPercentage

                binding.selectedMarkerBackgroundView.updateTargetViewMargin(xMargin.toInt(), yMargin.toInt(), isSelectedView = true)
                binding.markerIconImageView.updateTargetViewMargin(xMargin.toInt(), yMargin.toInt(), isSelectedView = false)
                isDrawn = true
            }
        }

    private fun View.updateTargetViewMargin(xMargin: Int, yMargin: Int, isSelectedView: Boolean = true) {
        updateLayoutParams<LayoutParams> {
            marginStart = xMargin - (measuredWidth / if (isSelectedView) 3 else 1)
            topMargin = yMargin - (measuredHeight / if (isSelectedView) 3 else 1)
        }
    }

}
