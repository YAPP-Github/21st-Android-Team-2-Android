package com.yapp.itemfinder.feature.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.LayoutDefaultTopNavigationBinding
import com.yapp.itemfinder.feature.common.databinding.LayoutSearchTopNavigationBinding
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.visible

class DefaultTopNavigationView
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {

    private val binding: LayoutDefaultTopNavigationBinding =
        LayoutDefaultTopNavigationBinding.inflate(LayoutInflater.from(context), this, false)

    var backButtonImageResId: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                binding.backButtonImageView.setImageResource(value)
            }
        }

    var containerColor: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                binding.topNavigationContainer.setBackgroundColor(value)
            }
        }

    var titleText: CharSequence?
        get() = binding.titleTextView.text
        set(value) {
            binding.titleTextView.text = value
        }

    var rightSecondIcon: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                binding.rightSecondIcon.visible()
                binding.rightSecondIcon.background =
                    ContextCompat.getDrawable(context, value)
            } else {
                binding.rightSecondIcon.gone()
            }
        }

    var rightFirstIcon: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                binding.rightSecondIcon.visible()
                binding.rightSecondIcon.background =
                    ContextCompat.getDrawable(context, value)
            } else {
                binding.rightSecondIcon.gone()
            }
        }

    init {
        backButtonImageResId = R.drawable.ic_back
    }
}
