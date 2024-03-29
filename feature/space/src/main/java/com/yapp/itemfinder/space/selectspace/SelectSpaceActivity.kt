package com.yapp.itemfinder.space.selectspace

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.Depth
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

    override val depth: Depth
        get() = Depth.SECOND

    override val vm by viewModels<SelectSpaceViewModel>()

    override val binding by viewBinding(ActivitySelectSpaceBinding::inflate)

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    private var dataListAdapter: DataListAdapter<Data>? = null

    override fun initViews() = with(binding) {
        initToolbar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
        }
        recyclerView.adapter = dataListAdapter
    }

    private fun initToolbar() = with(binding.defaultNavigationView) {
        backButtonImageResId = CR.drawable.ic_back
        containerColor = CR.color.brown_02
        backButtonClickListener = {
            setSelectSpaceResult()
            finish()
        }
        titleText = "보관함 위치"
    }

    override fun observeData(): Job = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED){
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
                vm.sideEffectFlow.collect { sideEffect ->
                    when (sideEffect) {
                        is SelectSpaceSideEffect.ShowToast -> {
                            Toast.makeText(this@SelectSpaceActivity, sideEffect.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
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

    private fun setSelectSpaceResult() {
        val spaceId = vm.getNewSpaceId()
        val spaceName = vm.getNewSpaceName()
        intent.apply {
            putExtra(AddLockerActivity.NEW_SPACE_ID, spaceId)
            putExtra(AddLockerActivity.NEW_SPACE_NAME, spaceName)
        }
        setResult(RESULT_OK, intent)
    }

    override fun onBackPressed() {
        setSelectSpaceResult()
        super.onBackPressed()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SelectSpaceActivity::class.java)
    }

}
