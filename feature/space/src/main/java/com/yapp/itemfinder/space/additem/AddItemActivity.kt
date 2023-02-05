package com.yapp.itemfinder.space.additem

import android.app.ActionBar.LayoutParams
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.*
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.parcelable
import com.yapp.itemfinder.feature.common.views.SnackBarView
import com.yapp.itemfinder.space.additem.itemposition.AddItemPositionDefineActivity
import com.yapp.itemfinder.space.additem.selectspace.AddItemSelectSpaceActivity
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.space.databinding.ActivityAddItemBinding
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddItemActivity : BaseStateActivity<AddItemViewModel, ActivityAddItemBinding>() {

    override val vm by viewModels<AddItemViewModel>()

    override val binding by viewBinding(ActivityAddItemBinding::inflate)

    override val depth: Depth
        get() = Depth.THIRD

    private var dataListAdapter: DataListAdapter<Data>? = null

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    private val spaceAndLockerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.parcelable<SpaceAndLockerEntity>(SELECTED_SPACE_AND_LOCKER_KEY)?.let {
                    vm.setSelectedSpaceAndLocker(it)
                }
            }
        }

    private val itemPositionDefineLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.parcelable<LockerAndItemEntity>(LOCKER_AND_ITEM_KEY)?.let {
                vm.setDefinedLockerAndItem(it)
            }
        }
    }

    override fun initViews() = with(binding) {
        initToolBar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
            recyclerView.itemAnimator = null
        }
        supportFragmentManager.setFragmentResultListener(
            CHECKED_CATEGORY_REQUEST_KEY,
            this@AddItemActivity
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
        when (intent.getStringExtra(SCREEN_MODE)) {
            ScreenMode.ADD_MODE.label -> titleText = "물건 추가"
            ScreenMode.EDIT_MODE.label -> titleText = "물건 수정"
        }
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
                            val dateListener =
                                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                    val date =
                                        String.format("%d.%02d.%02d.", year, month + 1, dayOfMonth)
                                    vm.setExpirationDate(date)
                                }
                            openDatePickerDialog(dateListener, "소비기한")
                        }
                        is AddItemSideEffect.OpenPurchaseDatePicker -> {
                            val dateListener =
                                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                    val date =
                                        String.format("%d.%02d.%02d.", year, month + 1, dayOfMonth)
                                    vm.setPurchaseDate(date)
                                }
                            openDatePickerDialog(dateListener, "구매일")
                        }
                        is AddItemSideEffect.FillOutRequiredSnackBar -> {
                            SnackBarView.make(binding.root, "물건 이름, 카테고리, 위치는 필수 항목이에요.").show()
                        }
                        is AddItemSideEffect.FillOutNameSnackBar -> {
                            SnackBarView.make(binding.root, "물건의 이름을 입력해주세요.").show()
                        }
                        is AddItemSideEffect.FillOutCategorySnackBar -> {
                            SnackBarView.make(binding.root, "물건의 카테고리를 선택해주세요.").show()
                        }
                        is AddItemSideEffect.FillOutLocationSnackBar -> {
                            SnackBarView.make(binding.root, "물건을 보관할 위치를 선택해주세요.").show()
                        }
                        is AddItemSideEffect.NameLengthLimitSnackBar -> {
                            SnackBarView.make(binding.root, "물건 이름은 한글 기준 최대 30자까지 작성 가능해요.").show()
                        }
                        is AddItemSideEffect.MemoLengthLimitSnackBar -> {
                            SnackBarView.make(binding.root, "메모는 한글 기준 최대 200자까지 작성 가능해요").show()
                        }
                        is AddItemSideEffect.OpenPhotoPicker -> {
                            val addItemImages = sideEffect.addItemImages
                            TedImagePicker.with(this@AddItemActivity)
                                .max(
                                    addItemImages.maxCount, "${addItemImages.maxCount}개초과!"
                                ).selectedUri(addItemImages.uriStringList.map { Uri.parse(it) })
                                .startMultiImage { uris ->
                                    vm.doneChooseImages(uris)
                                }
                        }
                        is AddItemSideEffect.MoveSelectSpace -> {
                            spaceAndLockerLauncher.launch(
                                AddItemSelectSpaceActivity.newIntent(
                                    this@AddItemActivity,
                                    sideEffect.spaceAndLockerEntity
                                )
                            )
                        }
                        is AddItemSideEffect.MoveItemPositionDefine -> {
                            itemPositionDefineLauncher.launch(
                                AddItemPositionDefineActivity.newIntent(this@AddItemActivity, sideEffect.lockerAndItemEntity)
                            )
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
            this@AddItemActivity,
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

    private fun handleLoading(addLockerState: AddItemState) {

    }

    private fun handleSuccess(addLockerState: AddItemState.Success) {
        if (addLockerState.isRefreshNeed.not()) return
        dataBindHelper.bindList(addLockerState.dataList, vm)
        dataListAdapter?.submitList(addLockerState.dataList)
    }

    private fun handleError(addLockerState: AddItemState.Error) {

    }

    companion object {
        const val SELECT_CATEGORY_DIALOG = "SELECT_CATEGORY_DIALOG"
        const val CHECKED_CATEGORY_REQUEST_KEY = "CHECKED_CATEGORY_REQUEST_KEY"
        const val CHECKED_CATEGORY_KEY = "CHECKED_CATEGORY_KEY"

        const val SELECTED_SPACE_AND_LOCKER_KEY = "SELECTED_SPACE_AND_LOCKER_KEY"
        const val SCREEN_MODE = "SCREEN_MODE"
        const val LOCKER_AND_ITEM_KEY = "LOCKER_AND_ITEM_KEY"

        fun newIntent(context: Context) = Intent(context, AddItemActivity::class.java)

    }

}
