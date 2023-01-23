package com.yapp.itemfinder.space.addlocker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.ContextThemeWrapper
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.space.databinding.ActivityAddLockerBinding
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
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
        }
    }

    override fun observeData(): Job = lifecycleScope.launch {
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
                        showAddPhotoDialog()
                    }
                    is AddLockerSideEffect.ShowToast -> {
                        showShortToast(sideEffect.message)
                    }
                }
            }
        }
    }

    private fun showAddPhotoDialog() {
        AlertDialog.Builder(
            ContextThemeWrapper(
                this@AddLockerActivity,
                CR.style.AlertDialog
            )
        )
            .setTitle(getString(R.string.attach_photo))
            .setMessage(getString(R.string.choose_how_to_attach_photo))
            .setPositiveButton(getString(R.string.camera)) { _, _ ->
                uploadByCamera()
            }
            .setNegativeButton(getString(R.string.gallery)) { _, _ ->
                uploadByGallery()
            }
            .create()
            .show()
    }

    private fun uploadByCamera() {
        // 사진 가져오기
        // 크로핑
        // 이미지뷰에 저장
    }

    private fun uploadByGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun handleLoading(addLockerState: AddLockerState) {
    }

    private fun handleSuccess(addLockerState: AddLockerState.Success) {
        dataBindHelper.bindList(addLockerState.dataList, vm)
        dataListAdapter?.submitList(addLockerState.dataList)
    }

    private fun handleError(addLockerState: AddLockerState.Error) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                val uri = data?.data
                if (uri != null) {
                    vm.uploadImage(uri)
                } else {
                    showShortToast(getString(R.string.failed_get_photo))
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AddLockerActivity::class.java)
        const val GALLERY_REQUEST_CODE = 2020
    }
}
