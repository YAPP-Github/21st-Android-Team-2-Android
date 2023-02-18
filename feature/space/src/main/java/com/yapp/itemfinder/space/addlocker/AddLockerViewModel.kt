package com.yapp.itemfinder.space.addlocker

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.LockerRepositoryQualifiers
import com.yapp.itemfinder.domain.model.*
import com.yapp.itemfinder.domain.repository.ImageRepository
import com.yapp.itemfinder.domain.repository.LockerRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddLockerViewModel @Inject constructor(
    @LockerRepositoryQualifiers
    private val lockerRepository: LockerRepository,
    private val imageRepository: ImageRepository,
    @ApplicationContext private val applicationContext: Context,
    val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<AddLockerState, AddLockerSideEffect>() {
    override val _stateFlow: MutableStateFlow<AddLockerState> =
        MutableStateFlow(AddLockerState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<AddLockerSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(AddLockerState.Loading)
        val screenMode = savedStateHandle.get<String>(AddLockerActivity.SCREEN_MODE)
        when (screenMode) {
            ScreenMode.ADD_MODE.label -> {
                setState(
                    AddLockerState.Success(
                        listOf(
                            AddLockerNameInput(mode = ScreenMode.ADD_MODE),
                            AddLockerSpace(
                                name = savedStateHandle.get<String>(AddLockerActivity.SPACE_NAME_KEY)
                                    ?: ""
                            ),
                            LockerIcons(mode = ScreenMode.ADD_MODE),
                            AddLockerPhoto(mode = ScreenMode.ADD_MODE)
                        ),
                        lockerName = "",
                        spaceId = savedStateHandle.get<Long>(AddLockerActivity.SPACE_ID_KEY) ?: 0,
                        icon = LockerIconId.LOCKER_ICON1.iconId,
                        url = null,
                        lockerId = null
                    )
                )
            }
            ScreenMode.EDIT_MODE.label -> {
                val locker =
                    savedStateHandle.get<Parcelable>(AddLockerActivity.LOCKER_ENTITY_KEY) as LockerEntity
                setState(
                    AddLockerState.Success(
                        listOf(
                            AddLockerNameInput(name = locker.name, mode = ScreenMode.EDIT_MODE),
                            AddLockerSpace(
                                name = savedStateHandle.get<String>(AddLockerActivity.SPACE_NAME_KEY)
                                    ?: ""
                            ),
                            LockerIcons(icon = locker.icon, mode = ScreenMode.EDIT_MODE),
                            AddLockerPhoto(url = locker.imageUrl, mode = ScreenMode.EDIT_MODE)
                        ),
                        lockerName = locker.name,
                        spaceId = savedStateHandle.get<Long>(AddLockerActivity.SPACE_ID_KEY) ?: 0,
                        icon = LockerIconId.LOCKER_ICON1.iconId,
                        url = locker.imageUrl,
                        lockerId = locker.id
                    )
                )
            }
        }
    }

    fun openSelectSpace() {
        withState<AddLockerState.Success> {
            postSideEffect(AddLockerSideEffect.OpenSelectSpace)
        }
    }

    fun getSpaceId(): Long {
        var id = 0L
        withState<AddLockerState.Success> { state ->
            id = state.spaceId
        }
        return id
    }

    fun setLockerName(name: String) {
        withState<AddLockerState.Success> { state ->
            setState(state.copy(lockerName = name))
        }
    }

    fun setLockerIcon(icon: String) {
        withState<AddLockerState.Success> { state ->
            setState(state.copy(icon = icon))
        }
    }

    fun changeSpace(name: String, spaceId: Long) {
        withState<AddLockerState.Success> { state ->
            setState(
                state.copy(
                    spaceId = spaceId,
                    dataList = ArrayList(state.dataList).apply {
                        removeAt(1)
                        add(1, AddLockerSpace(name = name))
                    },
                    isRefreshNeed = true
                )
            )
        }
    }

    fun addNewLocker() = viewModelScope.launch {
        withState<AddLockerState.Success> { state ->
            state.dataList
                .filterIsInstance<AddLockerNameInput>()
                .firstOrNull()?.saveName()
        }
        withState<AddLockerState.Success> { state ->
            runCatchingWithErrorHandler {
                lockerRepository.addLocker(
                    name = state.lockerName,
                    spaceId = state.spaceId,
                    icon = state.icon,
                    url = state.url
                )
            }.onSuccess { locker ->
                postSideEffect(AddLockerSideEffect.SuccessRegister)
            }.onErrorWithResult { errorWithResult ->
                setState(AddLockerState.Error(errorWithResult))
                val message = errorWithResult.errorResultEntity.message
                message?.let { postSideEffect(AddLockerSideEffect.ShowToast(it)) }
            }
        }
    }

    fun editLocker() = viewModelScope.launch {
        withState<AddLockerState.Success> { state ->
            state.dataList
                .filterIsInstance<AddLockerNameInput>()
                .firstOrNull()?.saveName()
        }
        withState<AddLockerState.Success> { state ->
            runCatchingWithErrorHandler {
                lockerRepository.editLocker(
                    name = state.lockerName,
                    spaceId = state.spaceId,
                    icon = state.icon,
                    url = state.url,
                    lockerId = state.lockerId!!
                )
            }.onErrorWithResult { errorWithResult ->
                setState(AddLockerState.Error(errorWithResult))
                val message = errorWithResult.errorResultEntity.message
                message?.let { postSideEffect(AddLockerSideEffect.ShowToast(it)) }
            }
        }
    }

    fun addImage() {
        withState<AddLockerState.Success> {
            postSideEffect(AddLockerSideEffect.UploadImage)
        }
    }

    fun uploadImage(uri: Uri): Job = viewModelScope.launch {
        // 실제구현: 서버 업로드 성공할 경우 해당 url로 set해주기

//        Log.i("TAG", "uploadImage: $imageUrl")
        withState<AddLockerState.Success> { successState ->
            runCatchingWithErrorHandler {
                setState(AddLockerState.Loading)
                val filePath = uri.cropToJpeg(applicationContext, 4, 3)
                val imageUrl = imageRepository.addImages(listOf(filePath)).first()
                imageUrl
            }.onSuccess {

                val idx = successState.dataList.indexOfFirst { data -> data is AddLockerPhoto }
                val newDataList = successState.dataList.toMutableList().apply {
                    this[idx] = AddLockerPhoto(url = it, mode = ScreenMode.EDIT_MODE)
                }
                setState(
                    successState.copy(
                        dataList = newDataList,
                        isRefreshNeed = true,
                        url = it
                    )
                )
            }.onErrorWithResult {
                setState(AddLockerState.Error(it))
                val message = it.errorResultEntity.message ?: return@launch
                postSideEffect(AddLockerSideEffect.ShowToast(message))

            }
        }
    }
}
