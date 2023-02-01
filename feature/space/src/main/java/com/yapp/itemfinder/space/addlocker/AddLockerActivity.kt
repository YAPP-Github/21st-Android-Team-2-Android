package com.yapp.itemfinder.space.addlocker

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.space.databinding.ActivityAddLockerBinding
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
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
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
        }
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
                            // 보관함 위치(selectSpace) 화면으로 이동
                            val name = "서재"
                            vm.changeSpace(name)
                        }
                        is AddLockerSideEffect.UploadImage -> {
                            handleUploadImage()
                        }
                        is AddLockerSideEffect.ShowToast -> {
                            showShortToast(sideEffect.message)
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

    private fun handleUploadImage() {
        TedImagePicker.with(this)
            .start { vm.uploadImage(it)  }
    }

    companion object{
        fun newIntent(context: Context) = Intent(context, AddLockerActivity::class.java)
    }
}
