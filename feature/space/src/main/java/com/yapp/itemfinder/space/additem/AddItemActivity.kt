package com.yapp.itemfinder.space.additem

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.space.databinding.ActivityAddItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddItemActivity : BaseStateActivity<AddItemViewModel, ActivityAddItemBinding>() {

    override val vm by viewModels<AddItemViewModel>()

    override val binding by viewBinding(ActivityAddItemBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        initToolBar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
        }
        supportFragmentManager.setFragmentResultListener(
            CHECKED_CATEGORY_REQUEST_KEY,
            this@AddItemActivity
        ) { requestKey, bundle ->
            val newCategoryName = bundle.getString(CHECKED_CATEGORY_KEY)
            if (newCategoryName != null) {
                vm.setCategory(newCategoryName)
            }
        }
    }

    private fun initToolBar() = with(binding.defaultNavigationView) {
        backButtonImageResId = CR.drawable.ic_close_round
        containerColor = CR.color.brown_03
        backButtonClickListener = {
            // 잠시만요! 팝업
            finish()
        }
        titleText = "물건 추가"
        rightFirstIcon = CR.drawable.ic_done
        rightFirstIconClickListener = {
            Toast.makeText(this@AddItemActivity, "save click", Toast.LENGTH_SHORT).show()
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
                    is AddItemSideEffect.OpenExpirationDatePicker -> {
                        val cal = Calendar.getInstance()
                        val dateListener =
                            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                val date =
                                    String.format("%d.%02d.%02d.", year, month + 1, dayOfMonth)
                                vm.setExpirationDate(date)
                            }
                        val dialog = DatePickerDialog(
                            this@AddItemActivity,
                            dateListener,
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        )
                        dialog.show()
                    }
                    is AddItemSideEffect.OpenPurchaseDatePicker -> {
                        val cal = Calendar.getInstance()
                        val dateListener =
                            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                val date =
                                    String.format("%d.%02d.%02d.", year, month + 1, dayOfMonth)
                                vm.setPurchaseDate(date)
                            }
                        val dialog = DatePickerDialog(
                            this@AddItemActivity,
                            dateListener,
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        )
                        dialog.show()
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
        const val CHECKED_CATEGORY_REQUEST_KEY = "CHECKED_CATEGORY_REQUEST_KEY"
        const val CHECKED_CATEGORY_KEY = "CHECKED_CATEGORY_KEY"
        fun newIntent(context: Context) = Intent(context, AddItemActivity::class.java)
    }

}
