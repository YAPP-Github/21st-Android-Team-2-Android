package com.yapp.itemfinder.space.addlocker

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.space.databinding.ActivityAddLockerBinding
import com.yapp.itemfinder.space.selectspace.SelectSpaceActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddLockerActivity : BaseStateActivity<AddLockerViewModel, ActivityAddLockerBinding>() {

    override val vm by viewModels<AddLockerViewModel>()

    override val binding by viewBinding(ActivityAddLockerBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        initToolbar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
        }
    }

    private fun initToolbar() = with(binding.defaultNavigationView){
        backButtonImageResId = R.drawable.ic_close_round
        containerColor = R.color.brown_03
        backButtonClickListener = {
            finish()
        }
        titleText = "보관함 추가"
        rightFirstIcon = R.drawable.ic_done
    }

    override fun observeData(): Job = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED){
            launch {
                vm.stateFlow.collect { state ->
                    when (state) {
                        is AddLockerState.Uninitialized -> Unit
                        is AddLockerState.Loading -> handleLoading(state)
                        is AddLockerState.Success -> handleSuccess(state)
                        is AddLockerState.Error -> handleError(state)
                    }
                }
            }
            launch {
                vm.sideEffectFlow.collect { sideEffect ->
                    when (sideEffect) {
                        is AddLockerSideEffect.OpenSelectSpace -> {
                            val intent = SelectSpaceActivity.newIntent(this@AddLockerActivity)
                            intent.putExtra(SPACE_ID_KEY, 2L) // 현재 locker의 spacdId로 설정
                            startActivity(intent)
                            // 수정된 보관함 위치로 변경
                            // val name = "서재"
                            // vm.changeSpace(name)
                        }
                    }
                }
            }
        }
    }

    private fun handleLoading(addLockerState: AddLockerState) {

    }

    private fun handleSuccess(addLockerState: AddLockerState.Success) {
        dataBindHelper.bindList(addLockerState.dataList, vm)
        dataListAdapter?.submitList(addLockerState.dataList)
    }

    private fun handleError(addLockerState: AddLockerState.Error) {

    }

    companion object {
        const val SPACE_ID_KEY = "SPACE_ID_KEY"
        fun newIntent(context: Context) = Intent(context, AddLockerActivity::class.java)
    }
}
