package com.yapp.itemfinder.feature.common.views.item

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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

    var selected: Boolean? = null
        set(value) {
            field = value
            binding.selectedItemGroup.isVisible = isSelected
            binding.markerIconImageView.isVisible = !isSelected
        }

    var position: Item.Position? = null
        set(value) {
            field = value
            val targetView = if (selected == true) {
                binding.selectedItemGroup.visible()
                binding.markerIconImageView.gone()
                binding.selectedMarkerBackgroundView
            } else {
                binding.selectedItemGroup.gone()
                binding.markerIconImageView.visible()
                binding.markerIconImageView
            }
            val xPercentage = (position?.x ?: 0f) / 100f
            val yPercentage = (position?.y ?: 0f) / 100f

            doOnGlobalLayout {
                val parentView = (parent as ConstraintLayout)
                val xMargin = parentView.measuredWidth * if (xPercentage > 0.95) 0.95f else xPercentage
                val yMargin = parentView.measuredHeight * if (yPercentage > 0.95) 0.95f else yPercentage

                targetView.updateLayoutParams<LayoutParams> {
                    marginStart = xMargin.toInt() - (targetView.measuredWidth / 2)
                    topMargin = yMargin.toInt() - (targetView.measuredHeight / 2)
                }

            }
        }

}
