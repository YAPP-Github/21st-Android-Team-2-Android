package com.yapp.itemfinder.space.selectspace

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.space.addlocker.AddLockerActivity
import com.yapp.itemfinder.space.databinding.ActivitySelectSpaceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectSpaceActivity : BaseStateActivity<SelectSpaceViewModel, ActivitySelectSpaceBinding>() {

    override val vm by viewModels<SelectSpaceViewModel>()

    override val binding by viewBinding(ActivitySelectSpaceBinding::inflate)

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    private var dataListAdapter: DataListAdapter<Data>? = null

    override fun initViews() = with(binding) {
        initToolbar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
        }
    }

    private fun initToolbar() = with(binding.defaultNavigationView) {
        backButtonImageResId = CR.drawable.ic_back
        containerColor = CR.color.brown_02
        backButtonClickListener = {
            val spaceId = vm.getNewSpaceId()
            val spaceName = vm.getNewSpaceName()
            intent.apply {
                putExtra(AddLockerActivity.NEW_SPACE_ID, spaceId)
                putExtra(AddLockerActivity.NEW_SPACE_NAME, spaceName)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        titleText = "보관함 위치"
    }

    override fun observeData(): Job = lifecycleScope.launch {
        launch {
            vm.stateFlow.collect { state ->
                when (state) {
                    is SelectSpaceState.Uninitialized -> Unit
                    is SelectSpaceState.Loading -> handleLoading(state)
                    is SelectSpaceState.Success -> handleSuccess(state)
                    is SelectSpaceState.Error -> handleError(state)
                }
            }
        }
        launch {
            vm.sideEffectFlow.collect {

            }
        }

    }

    private fun handleLoading(selectSpaceState: SelectSpaceState) {

    }

    private fun handleSuccess(selectSpaceState: SelectSpaceState.Success) {
        dataBindHelper.bindList(selectSpaceState.dataList, vm)
        dataListAdapter?.submitList(selectSpaceState.dataList)
    }

    private fun handleError(selectSpaceState: SelectSpaceState) {

    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SelectSpaceActivity::class.java)
    }

}
