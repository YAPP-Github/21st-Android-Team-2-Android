package com.yapp.itemfinder.space.additem.addtag

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
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

    }

    private fun initToolbar() = with(binding.defaultNavigationView) {
        backButtonImageResId = R.drawable.ic_back
        containerColor = R.color.brown_02
        titleText = getString(R.string.tag)
        backButtonClickListener = {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        rightFirstIcon = R.drawable.ic_done
    }

    override fun observeData(): Job = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                vm.stateFlow.collect { state ->

                }
            }
            launch {
                vm.sideEffectFlow.collect { sideEffect ->
                    when (sideEffect) {
                        is AddTagSideEffect.AddTags -> handleAddTags(sideEffect)
                    }
                }
            }
        }
    }

    private fun handleLoading() {
    }

    private fun handleSuccess(addLockerState: AddTagState.Success) {
        dataBindHelper.bindList(addLockerState.dataList, vm)
        dataListAdapter?.submitList(addLockerState.dataList)
    }

    private fun handleError(addLockerState: AddTagState.Error) {

    }

    private fun handleAddTags(sideEffect: AddTagSideEffect.AddTags) {

    }

    companion object {
        fun newIntent(context: Context, selectedTagList: List<Tag>) =
            Intent(context, AddTagActivity::class.java).apply {
                putParcelableArrayListExtra(SELECTED_TAGS_KEY, ArrayList(selectedTagList))
            }
    }
}
