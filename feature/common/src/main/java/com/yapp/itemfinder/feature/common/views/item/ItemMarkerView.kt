package com.yapp.itemfinder.feature.common.views.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.feature.common.R
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
                val xMargin = parentView.measuredWidth * xPercentage
                val yMargin = parentView.measuredHeight * yPercentage

                val maxWidth = context.dimen(R.dimen.marker_map_view_max_width).toInt()
                val adjustedRatio = maxWidth / parentView.measuredWidth.toFloat()
                val additionalWidthMargin = (parentView.measuredWidth - maxWidth) / 2
                val adjustedXMargin = if (parentView.measuredWidth > maxWidth) xMargin * adjustedRatio + additionalWidthMargin else xMargin

                val selectedMarkerXMargin = adjustedXMargin.toInt() - binding.selectedMarkerBackgroundView.measuredWidth / 2
                val markerXMargin = adjustedXMargin.toInt() - binding.markerIconImageView.measuredWidth / 2


                binding.selectedMarkerBackgroundView.updateTargetViewMargin(
                    parentView,
                    if (additionalWidthMargin > selectedMarkerXMargin) additionalWidthMargin
                    else selectedMarkerXMargin,
                    yMargin.toInt(),
                    isSelectedView = true
                )
                binding.markerIconImageView.updateTargetViewMargin(
                    parentView,
                    if (additionalWidthMargin > selectedMarkerXMargin) additionalWidthMargin
                    else selectedMarkerXMargin,
                    yMargin.toInt(),
                    isSelectedView = false
                )
                isDrawn = true
            }
        }

    private fun View.updateTargetViewMargin(parentView: View, xMargin: Int, yMargin: Int, isSelectedView: Boolean = true) {
        updateLayoutParams<LayoutParams> {
            marginStart = xMargin - (measuredWidth / if (isSelectedView) 3 else 1)
            topMargin = yMargin - if (isSelectedView) -(measuredHeight / 3) else measuredHeight
            if (marginStart + measuredWidth > parentView.measuredWidth) {
                marginStart = parentView.measuredWidth - measuredWidth
            }
            if (topMargin + measuredHeight > parentView.measuredHeight) {
                topMargin = parentView.measuredHeight - measuredHeight
            }
        }
    }

}
