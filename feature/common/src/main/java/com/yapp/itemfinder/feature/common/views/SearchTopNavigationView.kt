package com.yapp.itemfinder.feature.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.yapp.itemfinder.domain.model.ActionHandler
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.LayoutSearchTopNavigationBinding

class SearchTopNavigationView
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {

    private val binding: LayoutSearchTopNavigationBinding =
        LayoutSearchTopNavigationBinding.inflate(LayoutInflater.from(context), this, true)

    var leftButtonImageResId: Int = -1
        set(value) {
            field = value
            if (field != -1) {
                binding.leftButtonImageView.setImageDrawable(ContextCompat.getDrawable(context, value))
            }
        }

    var leftButtonClickListener: ActionHandler? = null
        set(value) {
            field = value
            binding.leftButtonImageView.setOnClickListener { value?.invoke() }
        }

    var backgroundColorId: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                binding.topNavigationContainer.setBackgroundColor(ContextCompat.getColor(context, value))
            }
        }

    var searchBarImageResId: Int = -1
        set(value) {
            field = value
            if (field != -1) {
                binding.searchBarImageView.setImageDrawable(ContextCompat.getDrawable(context, value))
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
                binding.searchBarTextView.setTextColor(ContextCompat.getColor(context, value))
            }
        }

    init {
        backgroundColorId = R.color.white
        leftButtonImageResId = R.drawable.ic_menu
        searchBarBackgroundResId = R.drawable.bg_button_brown_02_radius_8
    }
}
