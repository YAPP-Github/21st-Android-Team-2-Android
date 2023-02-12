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
        invisible()
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

                val adjustedXPercentage =
                    xPercentage * (parentView.measuredWidth - (binding.markerBackgroundView.measuredWidth / 2)) / parentView.measuredWidth.toFloat()
                val adjustedYPercentage =
                    yPercentage * (parentView.measuredHeight - (binding.markerBackgroundView.measuredHeight / 2)) / parentView.measuredHeight.toFloat()


                val yMargin = parentView.measuredHeight * adjustedYPercentage

                val maxWidth = context.dimen(R.dimen.marker_map_view_max_width).toInt()

                if (parentView.measuredWidth > maxWidth) {
                    val xMargin = parentView.measuredWidth * xPercentage
                    val additionalWidthMargin = (parentView.measuredWidth - maxWidth) / 2

                    val adjustedRatio = (maxWidth - binding.markerBackgroundView.measuredWidth) / parentView.measuredWidth.toFloat()

                    val adjustedXMargin = xMargin * adjustedRatio + additionalWidthMargin + binding.markerBackgroundView.measuredWidth / 2

                    binding.markerBackgroundView.updateTargetViewMargin(
                        adjustedXMargin.toInt(),
                        yMargin.toInt(),
                    )
                } else {
                    val xMargin = parentView.measuredWidth * adjustedXPercentage
                    binding.markerBackgroundView.updateTargetViewMargin(
                        xMargin.toInt(),
                        yMargin.toInt(),
                    )
                }
                isDrawn = true
                post {
                    visible()
                }
            }
        }

    private fun View.updateTargetViewMargin(xMargin: Int, yMargin: Int) {
        updateLayoutParams<LayoutParams> {
            val markerWidthRatio = binding.markerBackgroundView.measuredWidth / binding.markerIconImageView.measuredWidth.toFloat()
            val markerHeightRatio = binding.markerBackgroundView.measuredHeight / binding.markerIconImageView.measuredHeight.toFloat()

            marginStart = xMargin - (measuredWidth / markerWidthRatio).toInt()
            topMargin = yMargin - (measuredHeight / markerHeightRatio).toInt()
        }
    }

}
