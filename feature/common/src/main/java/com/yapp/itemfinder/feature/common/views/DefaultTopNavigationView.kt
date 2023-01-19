package com.yapp.itemfinder.feature.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.yapp.itemfinder.domain.model.ActionHandler
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
        LayoutDefaultTopNavigationBinding.inflate(LayoutInflater.from(context), this, true)

    var backButtonImageResId: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                binding.backButtonImageView.setImageDrawable(ContextCompat.getDrawable(context, value))
            }
        }

    var backButtonClickListener: ActionHandler? = null
        set(value) {
            field = value
            binding.backButtonImageView.setOnClickListener { value?.invoke() }
        }

    var containerColor: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                binding.topNavigationContainer.setBackgroundColor(ContextCompat.getColor(context, value))
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

    var rightSecondIconClickListener: ActionHandler? = null
        set(value) {
            field = value
            binding.rightSecondIcon.setOnClickListener { value?.invoke() }
        }

    var rightFirstIcon: Int = -1
        set(value) {
            field = value
            if (value != -1) {
                binding.rightFirstIcon.visible()
                binding.rightFirstIcon.background =
                    ContextCompat.getDrawable(context, value)
            } else {
                binding.rightFirstIcon.gone()
            }
        }

    var rightFirstIconClickListener: ActionHandler? = null
        set(value) {
            field = value
            binding.rightFirstIcon.setOnClickListener { value?.invoke() }
        }

    init {
        backButtonImageResId = R.drawable.ic_back
    }
}
