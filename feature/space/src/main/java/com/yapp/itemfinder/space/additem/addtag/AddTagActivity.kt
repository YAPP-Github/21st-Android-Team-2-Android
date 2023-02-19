package com.yapp.itemfinder.space.additem.addtag

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.onDone
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.feature.common.extension.visible
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.additem.AddItemActivity.Companion.SELECTED_TAGS_KEY
import com.yapp.itemfinder.space.databinding.ActivityAddTagBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddTagActivity : BaseStateActivity<AddTagViewModel, ActivityAddTagBinding>() {

    override val vm by viewModels<AddTagViewModel>()

    override val binding by viewBinding(ActivityAddTagBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    override val depth: Depth
        get() = Depth.SECOND

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        initToolbar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
        }
        recyclerView.adapter = dataListAdapter
        tagInput.onDone { tagName ->
            vm.addTag(tagName)
            tagInput.editableText.clear()
        }
    }

    private fun initToolbar() = with(binding.defaultNavigationView) {
        backButtonImageResId = CR.drawable.ic_back
        containerColor = CR.color.brown_02
        titleText = getString(R.string.tag)
        backButtonClickListener = {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        rightFirstIcon = CR.drawable.ic_done
        rightFirstIconClickListener = {
            vm.saveTags()
        }
    }

    override fun observeData(): Job = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                vm.stateFlow.collect { state ->
                    when (state) {
                        is AddTagState.Success -> handleSuccess(state)
                        else -> Unit
                    }
                }
            }
            launch {
                vm.sideEffectFlow.collect { sideEffect ->
                    when (sideEffect) {
                        is AddTagSideEffect.Save -> handleSaveTags(sideEffect)
                        is AddTagSideEffect.ShowToast -> showShortToast(sideEffect.message)
                    }
                }
            }
        }
    }

    private fun handleLoading() {
    }

    private fun handleSuccess(state: AddTagState.Success) {
        binding.tagChipGroup.removeAllViews()
        binding.addedTagListTextView.text = getString(R.string.added_tag_list, state.selectedTagList?.size ?: 0)
        if (state.selectedTagList.isNullOrEmpty().not()) {
            binding.tagChipGroup.visible()
            state.selectedTagList?.forEach { tag ->
                val tagChip = Chip(this)
                val chipDrawable = ChipDrawable.createFromAttributes(this, null, 0, CR.style.SelectedTagChip)
                binding.tagChipGroup.addView(
                    tagChip.apply {
                        id = ViewCompat.generateViewId()
                        setChipDrawable(chipDrawable)
                        setTextColor(ContextCompat.getColor(context, CR.color.gray_05))
                        text = tag.name
                        layoutDirection = View.LAYOUT_DIRECTION_RTL
                        setEnsureMinTouchTargetSize(false)
                        setOnClickListener {
                            vm.deselectTag(tag)
                        }
                    }
                )
            }
        } else {
            binding.tagChipGroup.gone()
        }
        dataBindHelper.bindList(state.dataList, vm)
        dataListAdapter?.submitList(state.dataList)
    }

    private fun handleError(state: AddTagState.Error) {

    }

    private fun handleSaveTags(sideEffect: AddTagSideEffect.Save) {
        setResult(RESULT_OK,
            Intent().apply {
                putParcelableArrayListExtra(SELECTED_TAGS_KEY, ArrayList(sideEffect.tagList))
            }
        )
        finish()
    }

    companion object {
        fun newIntent(context: Context, selectedTagList: List<Tag>) =
            Intent(context, AddTagActivity::class.java).apply {
                putParcelableArrayListExtra(SELECTED_TAGS_KEY, ArrayList(selectedTagList))
            }
    }
}
