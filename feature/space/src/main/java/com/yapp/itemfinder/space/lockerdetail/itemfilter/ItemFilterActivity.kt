package com.yapp.itemfinder.space.lockerdetail.itemfilter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.forEach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.yapp.itemfinder.domain.model.ItemCategory
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.feature.common.extension.visible
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.databinding.ActivityItemFilterBinding
import com.yapp.itemfinder.space.lockerdetail.LockerDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ItemFilterActivity : BaseStateActivity<ItemFilterViewModel, ActivityItemFilterBinding>() {

    override val vm by viewModels<ItemFilterViewModel>()

    override val binding by viewBinding(ActivityItemFilterBinding::inflate)

    override val depth: Depth
        get() = Depth.THIRD

    override fun initViews() = with(binding) {
        initToolbar()

        resetButton.setOnClickListener {
            vm.resetFilterCondition()
        }

        applyButton.setOnClickListener {
            vm.applyFilterCondition()
        }

        binding.orderRadioGroup.setOnCheckedChangeListener { radioGroup, id ->
            when(id) {
                 R.id.orderRecentButton -> ItemFilterCondition.Order.RECENT
                 R.id.orderOldButton -> ItemFilterCondition.Order.OLD
                 R.id.orderAscButton -> ItemFilterCondition.Order.ASC
                 R.id.orderDescButton -> ItemFilterCondition.Order.DESC
                else -> null
            }?.let {
                vm.selectOrder(it)
            }
        }

        binding.lifeCheckBox.setOnClickListener {
            vm.checkCategory(getCheckedCategories())
        }
        binding.foodCheckBox.setOnClickListener {
            vm.checkCategory(getCheckedCategories())
        }
        binding.fashionCheckBox.setOnClickListener {
            vm.checkCategory(getCheckedCategories())
        }
    }

    private fun getCheckedCategories(): List<ItemCategory> {
        return mutableListOf<ItemCategory>().apply {
            if (binding.lifeCheckBox.isChecked) add(ItemCategory.LIFE)
            if (binding.foodCheckBox.isChecked) add(ItemCategory.FOOD)
            if (binding.fashionCheckBox.isChecked) add(ItemCategory.FASHION)
        }
    }

    private fun initToolbar() = with(binding.defaultNavigationView) {
        backButtonImageResId = CR.drawable.ic_close
        containerColor = CR.color.brown_03
        titleText = getString(R.string.item_filter)
        backButtonClickListener = {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun observeData(): Job = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                vm.stateFlow.collect { state ->
                    when (state) {
                        is ItemFilterState.Success -> handleSuccess(state)
                        else -> Unit
                    }
                }
            }
            launch {
                vm.sideEffectFlow.collect { sideEffect ->
                    when (sideEffect) {
                        is ItemFilterSideEffect.Apply -> handleApplyCondition(sideEffect)
                        is ItemFilterSideEffect.Reset -> handleResetCondition(sideEffect)
                        is ItemFilterSideEffect.ShowToast -> showShortToast(sideEffect.message)
                    }
                }
            }
        }
    }

    private fun handleLoading() {
    }

    private fun handleSuccess(state: ItemFilterState.Success) {
        if (state.isRefreshNeed) {
            binding.tagChipGroup.removeAllViews()
            if (state.tagList.isNotEmpty()) {
                binding.tagTitleTextView.visible()
                binding.tagChipGroup.visible()
                state.tagList.forEach { tag ->
                    val tagChip = Chip(this)
                    val chipDrawable = ChipDrawable.createFromAttributes(this, null, 0, CR.style.CheckTagChip)
                    binding.tagChipGroup.addView(
                        tagChip.apply {
                            id = tag.id.toInt()
                            setChipDrawable(chipDrawable)
                            setTextColor(ContextCompat.getColor(context, CR.color.gray_05))
                            text = tag.name
                            layoutDirection = View.LAYOUT_DIRECTION_RTL
                            setEnsureMinTouchTargetSize(false)
                        }
                    )
                }
            } else {
                binding.tagTitleTextView.gone()
                binding.tagChipGroup.gone()
            }
        }
        applyCondition(state.appliedItemFilterCondition)
    }

    private fun applyCondition(appliedItemFilterCondition: ItemFilterCondition) {
        val (order, categories, tags) = appliedItemFilterCondition

        binding.orderRadioGroup.check(
            when(order) {
                ItemFilterCondition.Order.RECENT -> R.id.orderRecentButton
                ItemFilterCondition.Order.OLD -> R.id.orderOldButton
                ItemFilterCondition.Order.ASC -> R.id.orderAscButton
                ItemFilterCondition.Order.DESC -> R.id.orderDescButton
            }
        )

        binding.lifeCheckBox.isChecked = categories.contains(ItemCategory.LIFE)
        binding.foodCheckBox.isChecked = categories.contains(ItemCategory.FOOD)
        binding.fashionCheckBox.isChecked = categories.contains(ItemCategory.FASHION)

        binding.tagChipGroup.forEach { it as Chip
            val tag = tags.find { tag -> tag.id == it.id.toLong() }
            if (tag != null) {
                it.chipStrokeColor = ContextCompat.getColorStateList(this, CR.color.orange)
                it.setOnClickListener { vm.deselectTag(tag) }
            } else {
                it.chipStrokeColor = ContextCompat.getColorStateList(this, CR.color.gray_02)
                it.setOnClickListener { vm.selectTag(it.id.toLong()) }
            }
        }
    }

    private fun handleError(state: ItemFilterState.Error) {

    }

    private fun handleApplyCondition(sideEffect: ItemFilterSideEffect.Apply) {
        setResult(RESULT_OK,
            Intent().apply {
                putExtra(LockerDetailFragment.FILTER_RESULT_KEY, sideEffect.appliedItemFilterCondition)
            }
        )
        finish()
    }

    private fun handleResetCondition(sideEffect: ItemFilterSideEffect.Reset) {
        applyCondition(sideEffect.initialItemFilterCondition)
    }

    companion object {
        const val INITIAL_ITEM_FILTER_CONDITION = "INITIAL_ITEM_FILTER_CONDITION"

        fun newIntent(context: Context, initialItemFilterCondition: ItemFilterCondition) =
            Intent(context, ItemFilterActivity::class.java).apply {
                putExtra(INITIAL_ITEM_FILTER_CONDITION, initialItemFilterCondition)
            }
    }
}
