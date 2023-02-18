package com.yapp.itemfinder.space.addlocker

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.ScreenMode
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.space.additem.AddItemActivity
import com.yapp.itemfinder.space.databinding.ActivityAddLockerBinding
import com.yapp.itemfinder.space.selectspace.SelectSpaceActivity
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
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override val depth: Depth
        get() = Depth.THIRD

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        initToolbar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
        }
        recyclerView.adapter = dataListAdapter
        setResultNext()
        supportFragmentManager.setFragmentResultListener(
            ChangeLockerImageDialog.CONFIRM_KEY,
            this@AddLockerActivity
        ){ _, _ ->
            vm.changeImage()
        }
    }

    private fun initToolbar() = with(binding.defaultNavigationView) {
        backButtonImageResId = R.drawable.ic_close_round
        containerColor = R.color.brown_03
        backButtonClickListener = {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        rightFirstIcon = R.drawable.ic_done
        when (intent.getStringExtra(AddItemActivity.SCREEN_MODE)) {
            ScreenMode.ADD_MODE.label -> {
                titleText = "보관함 추가"
                rightFirstIconClickListener = {
                    vm.addNewLocker()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
            ScreenMode.EDIT_MODE.label -> {
                titleText = "보관함 수정"
                rightFirstIconClickListener = {
                    vm.editLocker()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    override fun observeData(): Job = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                            val id = vm.getSpaceId()
                            intent.putExtra(SPACE_ID_KEY, id) // 현재 locker의 spacdId로 설정
                            resultLauncher.launch(intent)
                        }
                        is AddLockerSideEffect.UploadImage -> {
                            handleUploadImage()
                        }
                        is AddLockerSideEffect.ShowToast -> {
                            showShortToast(sideEffect.message)
                        }
                        is AddLockerSideEffect.SuccessRegister -> {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                        is AddLockerSideEffect.OpenChangeImageDialog -> {
                            val dialog = ChangeLockerImageDialog.newInstance()
                            this@AddLockerActivity.supportFragmentManager.let { fm ->
                                dialog.show(fm, CHANGE_IMAGE_DIALOG)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleLoading(addLockerState: AddLockerState) {
    }

    private fun handleSuccess(addLockerState: AddLockerState.Success) {
        if (addLockerState.isRefreshNeed.not()) return
        dataBindHelper.bindList(addLockerState.dataList, vm)
        dataListAdapter?.submitList(addLockerState.dataList)
    }

    private fun handleError(addLockerState: AddLockerState.Error) {
    }

    private fun handleUploadImage() {
        TedImagePicker.with(this)
            .start { vm.uploadImage(it) }
    }

    private fun setResultNext() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val spaceId = result.data?.getLongExtra(NEW_SPACE_ID, 0)
                    val spaceName = result.data?.getStringExtra(NEW_SPACE_NAME)
                    if (spaceName != null && spaceName != "" && spaceId != null) {
                        vm.changeSpace(spaceName, spaceId)
                    }
                }
            }
    }

    companion object {
        const val SPACE_ID_KEY = "SPACE_ID_KEY"
        const val SPACE_NAME_KEY = "SPACE_NAME_KEY"
        const val NEW_SPACE_NAME = "NEW_SPACE_NAME_KEY"
        const val NEW_SPACE_ID = "NEW_SPACE_ID"
        const val SCREEN_MODE = "SCREEN_MODE"
        const val LOCKER_ENTITY_KEY = "LOCKER_ENTITY_KEY"
        const val CHANGE_IMAGE_DIALOG = "CHANGE_IMAGE_DIALOG"
        fun newIntent(context: Context) = Intent(context, AddLockerActivity::class.java)
    }
}
