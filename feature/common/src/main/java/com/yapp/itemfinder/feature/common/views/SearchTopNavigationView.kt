package com.yapp.itemfinder.feature.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.LayoutSearchTopNavigationBinding

class SearchTopNavigationView
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {

    private val binding: LayoutSearchTopNavigationBinding =
        LayoutSearchTopNavigationBinding.inflate(LayoutInflater.from(context), this, false)

    var rightButtonImageResId: Int = -1
        set(value) {
            field = value
            if (field != -1) {
                binding.rightButtonImageView.setImageResource(value)
            }
        }

    var searchBarBackgroundResId: Int = -1
        set(value) {
            field = value
            binding.searchBarBackgroundView.background =
                ContextCompat.getDrawable(context, value)
        }

    var searchBarText: CharSequence?
        get() = binding.searchBarTextView.text
        set(value) {
            binding.searchBarTextView.text = value
        }

    var searchBarTextColor: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                binding.searchBarTextView.setTextColor(value)
            }
        }

    init {
        rightButtonImageResId = R.drawable.ic_menu
        searchBarBackgroundResId = R.drawable.bg_button_brown_02_radius_8
    }
}
