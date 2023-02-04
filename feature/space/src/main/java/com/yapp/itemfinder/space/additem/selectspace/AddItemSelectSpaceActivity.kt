package com.yapp.itemfinder.space.additem.selectspace

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
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
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.additem.AddItemActivity.Companion.SELECTED_SPACE_AND_LOCKER_KEY
import com.yapp.itemfinder.space.additem.selectlocker.AddItemSelectLockerActivity
import com.yapp.itemfinder.space.databinding.ActivityAddItemSelectSpaceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddItemSelectSpaceActivity : BaseStateActivity<AddItemSelectSpaceViewModel, ActivityAddItemSelectSpaceBinding>() {

    override val depth: Depth
        get() = Depth.SECOND

    override val vm by viewModels<AddItemSelectSpaceViewModel>()

    override val binding by viewBinding(ActivityAddItemSelectSpaceBinding::inflate)

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    private var dataListAdapter: DataListAdapter<Data>? = null

    private val spaceAndLockerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.parcelable<SpaceAndLockerEntity>(SELECTED_SPACE_AND_LOCKER_KEY)?.let {
                setResult(
                    RESULT_OK, Intent().putExtra(SELECTED_SPACE_AND_LOCKER_KEY, it)
                )
                finish()
            }
        }
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
        titleText = getString(R.string.my_space)
    }

    override fun observeData(): Job = lifecycleScope.launch {
        launch {
            vm.stateFlow.collect { state ->
                when (state) {
                    is AddItemSelectSpaceState.Uninitialized -> Unit
                    is AddItemSelectSpaceState.Loading -> handleLoading(state)
                    is AddItemSelectSpaceState.Success -> handleSuccess(state)
                    is AddItemSelectSpaceState.Error -> handleError(state)
                }
            }
        }
        launch {
            vm.sideEffectFlow.collect { sideEffect ->
                when (sideEffect) {
                    is AddItemSelectSpaceSideEffect.MoveAddItemSelectLocker -> moveAddItemSelectLocker(sideEffect)
                }
            }
        }

    }

    private fun handleLoading(state: AddItemSelectSpaceState) {

    }

    private fun handleSuccess(state: AddItemSelectSpaceState.Success) {
        dataBindHelper.bindList(state.dataList, vm)
        dataListAdapter?.submitList(state.dataList)
    }

    private fun handleError(state: AddItemSelectSpaceState) {

    }

    private fun moveAddItemSelectLocker(sideEffect: AddItemSelectSpaceSideEffect.MoveAddItemSelectLocker) {
        spaceAndLockerLauncher.launch(
            AddItemSelectLockerActivity.newIntent(this, sideEffect.spaceAndLockerEntity)
        )
        showShortToast(sideEffect.spaceAndLockerEntity.toString())
    }

    companion object {

        fun newIntent(context: Context, selectedSpaceAndLockerEntity: SpaceAndLockerEntity?) =
            Intent(context, AddItemSelectSpaceActivity::class.java).apply {
                putExtra(SELECTED_SPACE_AND_LOCKER_KEY, selectedSpaceAndLockerEntity)
            }

    }

}
