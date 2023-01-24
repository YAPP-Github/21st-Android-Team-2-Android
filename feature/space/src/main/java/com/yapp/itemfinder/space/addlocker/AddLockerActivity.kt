package com.yapp.itemfinder.space.addlocker

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.ContextThemeWrapper
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.yapp.itemfinder.feature.common.utility.TakePictureWithUriReturnContract
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
    private val cameraPermission: String by lazy {
        Manifest.permission.CAMERA
    }

    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>


    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
        }
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                vm.uploadImage(it)
            }
        }

        cameraLauncher =
            registerForActivityResult(TakePictureWithUriReturnContract()) { (isSuccess, imageUrl) ->
                if (isSuccess) {
                    vm.uploadImage(imageUrl)
                }
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

    private fun handleLoading(addLockerState: AddLockerState) {
    }

    private fun handleSuccess(addLockerState: AddLockerState.Success) {
        dataBindHelper.bindList(addLockerState.dataList, vm)
        dataListAdapter?.submitList(addLockerState.dataList)
    }

    private fun handleError(addLockerState: AddLockerState.Error) {
    }

    /***
     * 외부저장소 읽기 권한을 확인하고, 허가된 경우 경우 다이얼로그를 보여줍니다.
     * 아닌 권한을 요청합니다.
     */

    private fun handleUploadImage() {
        showGetPhotoDialog()
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
                checkPermission(permission = cameraPermission) {
                    uploadByCamera()
                }
            }
            .setNegativeButton(getString(R.string.gallery)) { _, _ ->
                checkPermission(permission = imagePermission) {
                    uploadByGallery()
                }
            }
            .create()
            .show()
    }

    private fun checkPermission(permission: String, uploadAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                uploadAction()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                showPermissionContextPopup(permission)
            }
            else -> {
                doRequestPermission(permission)
            }
        }
    }

    private fun uploadByGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun uploadByCamera() {
        cameraLauncher.launch(contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues()))
    }

    private fun showPermissionContextPopup(permission: String) {
        AlertDialog.Builder(
            ContextThemeWrapper(
                this@AddLockerActivity,
                CR.style.AlertDialog
            )
        )
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                doRequestPermission(permission)

            }
            .create()
            .show()
    }

    private fun doRequestPermission(permission: String) {
        requestPermissions(
            arrayOf(permission),
            if (permission == imagePermission)
                PERMISSION_READ_IMAGE_CODE
            else
                PERMISSION_CAMERA_CODE
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
                    uploadByGallery()
                } else {
                    showShortToast(getString(CR.string.request_denied))
                }
            PERMISSION_CAMERA_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadByCamera()
                } else {
                    showShortToast(getString(CR.string.request_denied))
                }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AddLockerActivity::class.java)
        const val PERMISSION_READ_IMAGE_CODE = 1010
        const val PERMISSION_CAMERA_CODE = 1011
    }
}
