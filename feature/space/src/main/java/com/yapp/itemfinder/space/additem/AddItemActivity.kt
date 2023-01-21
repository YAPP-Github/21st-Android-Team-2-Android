package com.yapp.itemfinder.space.additem

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.space.databinding.ActivityAddItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddItemActivity : BaseStateActivity<AddItemViewModel, ActivityAddItemBinding>() {

    override val vm by viewModels<AddItemViewModel>()

    override val binding by viewBinding(ActivityAddItemBinding::inflate)

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
        launch {
            vm.stateFlow.collect { state ->
                when (state) {
                    is AddItemState.Uninitialized -> Unit
                    is AddItemState.Loading -> handleLoading(state)
                    is AddItemState.Success -> handleSuccess(state)
                    is AddItemState.Error -> handleError(state)
                }
            }
        }
        launch {
            vm.sideEffectFlow.collect { sideEffect ->
                when (sideEffect) {
                    is AddItemSideEffect.OpenSelectCategoryDialog -> {
                        val dialog = SelectCategoryDialog.getInstance()
                        this@AddItemActivity.supportFragmentManager?.let { fragmentManager ->
                            dialog.show(fragmentManager, SELECT_CATEGORY_DIALOG)
                        }

                    }
                }
            }
        }
    }

    private fun handleLoading(addLockerState: AddItemState) {

    }

    private fun handleSuccess(addLockerState: AddItemState.Success) {
        dataBindHelper.bindList(addLockerState.dataList, vm)
        dataListAdapter?.submitList(addLockerState.dataList)
    }

    private fun handleError(addLockerState: AddItemState.Error) {

    }

    companion object {
        const val SELECT_CATEGORY_DIALOG = "SELECT_CATEGORY_DIALOG"
        fun newIntent(context: Context) = Intent(context, AddItemActivity::class.java)
    }
}
