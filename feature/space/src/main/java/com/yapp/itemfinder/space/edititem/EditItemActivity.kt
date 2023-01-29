package com.yapp.itemfinder.space.edititem

import android.app.ActionBar.LayoutParams
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.ItemCategorySelection
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.views.SnackBarView
import com.yapp.itemfinder.space.additem.SelectCategoryDialog
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.space.databinding.ActivityAddItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EditItemActivity : BaseStateActivity<EditItemViewModel, ActivityAddItemBinding>() {

    override val vm by viewModels<EditItemViewModel>()

    override val binding by viewBinding(ActivityAddItemBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        initToolBar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
            recyclerView.itemAnimator = null
        }
        supportFragmentManager.setFragmentResultListener(
            CHECKED_CATEGORY_REQUEST_KEY,
            this@EditItemActivity
        ) { requestKey, bundle ->
            val newCategoryName = bundle.getString(CHECKED_CATEGORY_KEY)
            if (newCategoryName != null) {
                when (newCategoryName) {
                    ItemCategorySelection.LIFE.label -> vm.setCategory(ItemCategorySelection.LIFE)
                    ItemCategorySelection.FOOD.label -> vm.setCategory(ItemCategorySelection.FOOD)
                    ItemCategorySelection.FASHION.label -> vm.setCategory(ItemCategorySelection.FASHION)
                    else -> vm.setCategory(ItemCategorySelection.DEFAULT)
                }
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
            vm.saveItem()
        }
    }

    override fun observeData(): Job = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                vm.stateFlow.collect { state ->
                    when (state) {
                        is EditItemState.Uninitialized -> Unit
                        is EditItemState.Loading -> handleLoading(state)
                        is EditItemState.Success -> handleSuccess(state)
                        is EditItemState.Error -> handleError(state)
                    }
                }
            }
            launch {
                vm.sideEffectFlow.collect { sideEffect ->
                    when (sideEffect) {
                        is EditItemSideEffect.OpenSelectCategoryDialog -> {
                            val dialog = SelectCategoryDialog.getInstance()
                            this@EditItemActivity.supportFragmentManager?.let { fragmentManager ->
                                dialog.show(fragmentManager, SELECT_CATEGORY_DIALOG)
                            }
                        }
                        is EditItemSideEffect.OpenExpirationDatePicker -> {
                            val dateListener =
                                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                    val date =
                                        String.format("%d.%02d.%02d.", year, month + 1, dayOfMonth)
                                    vm.setExpirationDate(date)
                                }
                            openDatePickerDialog(dateListener, "소비기한")
                        }
                        is EditItemSideEffect.OpenPurchaseDatePicker -> {
                            val dateListener =
                                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                    val date =
                                        String.format("%d.%02d.%02d.", year, month + 1, dayOfMonth)
                                    vm.setPurchaseDate(date)
                                }
                            openDatePickerDialog(dateListener, "구매일")
                        }
                        is EditItemSideEffect.FillOutRequiredSnackBar -> {
                            SnackBarView.make(binding.root, "물건 이름, 카테고리, 위치는 필수 항목이에요.").show()
                        }
                        is EditItemSideEffect.FillOutNameSnackBar -> {
                            SnackBarView.make(binding.root, "물건의 이름을 입력해주세요.").show()
                        }
                        is EditItemSideEffect.FillOutCategorySnackBar -> {
                            SnackBarView.make(binding.root, "물건의 카테고리를 선택해주세요.").show()
                        }
                        is EditItemSideEffect.FillOutLocationSnackBar -> {
                            SnackBarView.make(binding.root, "물건을 보관할 위치를 선택해주세요.").show()
                        }
                        is EditItemSideEffect.NameLengthLimitSnackBar -> {
                            SnackBarView.make(binding.root, "물건 이름은 한글 기준 최대 30자까지 작성 가능해요.").show()
                        }
                        is EditItemSideEffect.MemoLengthLimitSnackBar -> {
                            SnackBarView.make(binding.root, "메모는 한글 기준 최대 200자까지 작성 가능해요").show()
                        }
                    }
                }
            }
        }
    }

    private fun openDatePickerDialog(
        dateListener: DatePickerDialog.OnDateSetListener,
        title: String
    ) {
        val cal = Calendar.getInstance()
        val dialog = DatePickerDialog(
            this@EditItemActivity,
            dateListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        dialog.setTitle(title)
        dialog.show()
        val positiveButton = dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
        positiveButton.setTextColor(getColor(CR.color.orange))
        positiveButton.text = "적용"
        val negativeButton = dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        negativeButton.setTextColor(getColor(CR.color.orange))
        negativeButton.text = "취소"
        dialog.window?.setLayout(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    private fun handleLoading(editItemState: EditItemState) {

    }

    private fun handleSuccess(editItemState: EditItemState.Success) {
        if (editItemState.isRefreshNeed.not()) return
        dataBindHelper.bindList(editItemState.dataList, vm)
        dataListAdapter?.submitList(editItemState.dataList)
    }

    private fun handleError(editItemState: EditItemState.Error) {

    }

    companion object {
        const val SELECT_CATEGORY_DIALOG = "SELECT_CATEGORY_DIALOG"
        const val CHECKED_CATEGORY_REQUEST_KEY = "CHECKED_CATEGORY_REQUEST_KEY"
        const val CHECKED_CATEGORY_KEY = "CHECKED_CATEGORY_KEY"
        fun newIntent(context: Context) = Intent(context, EditItemActivity::class.java)
    }

}