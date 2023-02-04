package com.yapp.itemfinder.space.additem.selectlocker

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.SpaceAndLockerEntity
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.parcelable
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.space.additem.AddItemActivity.Companion.SELECTED_SPACE_AND_LOCKER_KEY
import com.yapp.itemfinder.space.databinding.ActivityAddItemSelectSpaceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddItemSelectLockerActivity : BaseStateActivity<AddItemSelectLockerViewModel, ActivityAddItemSelectSpaceBinding>() {

    override val depth: Depth
        get() = Depth.SECOND

    override val vm by viewModels<AddItemSelectLockerViewModel>()

    override val binding by viewBinding(ActivityAddItemSelectSpaceBinding::inflate)

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    private var dataListAdapter: DataListAdapter<Data>? = null

    private val selectedSpaceTitle by lazy {
        intent.parcelable<SpaceAndLockerEntity>(SELECTED_SPACE_AND_LOCKER_KEY)?.manageSpaceEntity?.name
    }

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
        backButtonClickListener = { finish() }
        titleText = selectedSpaceTitle
    }

    override fun observeData(): Job = lifecycleScope.launch {
        launch {
            vm.stateFlow.collect { state ->
                when (state) {
                    is AddItemSelectLockerState.Uninitialized -> Unit
                    is AddItemSelectLockerState.Loading -> handleLoading(state)
                    is AddItemSelectLockerState.Success -> handleSuccess(state)
                    is AddItemSelectLockerState.Error -> handleError(state)
                }
            }
        }
        launch {
            vm.sideEffectFlow.collect { sideEffect ->
                when (sideEffect) {
                    is AddItemSelectLockerSideEffect.SavePath -> savePath(sideEffect)
                    is AddItemSelectLockerSideEffect.ShowToast -> showShortToast(sideEffect.message)
                }
            }
        }

    }

    private fun handleLoading(state: AddItemSelectLockerState) {

    }

    private fun handleSuccess(state: AddItemSelectLockerState.Success) {
        dataBindHelper.bindList(state.dataList, vm)
        dataListAdapter?.submitList(state.dataList)
    }

    private fun handleError(state: AddItemSelectLockerState) {

    }

    private fun savePath(sideEffect: AddItemSelectLockerSideEffect.SavePath) {
        setResult(
            RESULT_OK, Intent().putExtra(SELECTED_SPACE_AND_LOCKER_KEY, sideEffect.spaceAndLockerEntity)
        )
        finish()
    }

    companion object {

        fun newIntent(context: Context, spaceAndLockerEntity: SpaceAndLockerEntity) =
            Intent(context, AddItemSelectLockerActivity::class.java).apply {
                putExtra(SELECTED_SPACE_AND_LOCKER_KEY, spaceAndLockerEntity)
            }

    }

}
