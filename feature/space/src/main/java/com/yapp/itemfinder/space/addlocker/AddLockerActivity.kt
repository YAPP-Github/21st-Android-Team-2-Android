package com.yapp.itemfinder.space.addlocker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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

    private val imagePermission: String by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE
    }

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
                        handleUploadImage()
                    }
                    is AddLockerSideEffect.ShowToast -> {
                        showShortToast(sideEffect.message)
                    }
                }
            }
        }
    }

    /***
     * 외부저장소 읽기 권한을 확인하고, 허가된 경우 경우 다이얼로그를 보여줍니다.
     * 아닌 권한을 요청합니다.
     */

    private fun handleUploadImage() {
        checkExternalStorageOrMediaImages {
            showGetPhotoDialog()
        }
    }

    private fun checkExternalStorageOrMediaImages(uploadAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                imagePermission
            ) == PackageManager.PERMISSION_GRANTED -> {
                uploadAction()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionContextPopup()
            }
            else -> {
                doRequestPermissions()
            }
        }
    }

    private fun showGetPhotoDialog() {

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
        startActivityForResult(intent, GALLERY_INTENT_REQUEST_CODE)
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
            GALLERY_INTENT_REQUEST_CODE -> {
                val uri = data?.data
                if (uri != null) {
                    vm.uploadImage(uri)
                } else {
                    showShortToast(getString(R.string.failed_get_photo))
                }
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(
            ContextThemeWrapper(
                this@AddLockerActivity,
                CR.style.AlertDialog
            )
        )
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                doRequestPermissions()

            }
            .create()
            .show()
    }

    private fun doRequestPermissions() {
        requestPermissions(
            arrayOf(imagePermission),
            PERMISSION_READ_IMAGE_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_READ_IMAGE_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showGetPhotoDialog()
                } else {
                    Toast.makeText(this, getString(CR.string.request_denied), Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AddLockerActivity::class.java)
        const val GALLERY_INTENT_REQUEST_CODE = 2020
        const val PERMISSION_READ_IMAGE_CODE = 1010
    }
}
