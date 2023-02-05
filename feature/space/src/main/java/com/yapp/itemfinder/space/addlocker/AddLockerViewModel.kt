package com.yapp.itemfinder.space.addlocker

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.network.api.lockerlist.LockerApi
import com.yapp.itemfinder.domain.model.*
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.onErrorWithResult
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddLockerViewModel @Inject constructor(
    private val lockerApi: LockerApi,
    savedStateHandle: SavedStateHandle
) : BaseStateViewModel<AddLockerState, AddLockerSideEffect>() {
    override val _stateFlow: MutableStateFlow<AddLockerState> =
        MutableStateFlow(AddLockerState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<AddLockerSideEffect> = MutableSharedFlow()

    init {
        setState(
            AddLockerState.Success(
                listOf(
                    AddLockerNameInput(),
                    AddLockerSpace(
                        name = savedStateHandle.get<String>(AddLockerActivity.SPACE_NAME_KEY) ?: ""
                    ),
                    LockerIcons(),
                    AddLockerPhoto()
                ),
                lockerName = "",
                spaceId = savedStateHandle.get<Long>(AddLockerActivity.SPACE_ID_KEY) ?: 0,
                icon = LockerIconId.LOCKER_ICON1.iconId,
                url = null
            )
        )
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
            setState(state.copy(lockerName = name, isRefreshNeed = false))
        }
    }

    fun setLockerIcon(icon: String) {
        withState<AddLockerState.Success> { state ->
            setState(state.copy(icon = icon, isRefreshNeed = false))
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
                lockerApi.addNewLocker(
                    AddLockerRequest(
                        name = state.lockerName,
                        spaceId = state.spaceId,
                        icon = state.icon,
                        url = state.url
                    )
                )
            }.onSuccess { result ->

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
        // 실제구현: 서버 업로드가 성공할 경우
        withState<AddLockerState.Success> { successState ->
            runCatchingWithErrorHandler {
                setState(AddLockerState.Loading)

                val idx = successState.dataList.indexOfFirst { data -> data is AddLockerPhoto }
                val newDataList = successState.dataList.toMutableList().apply {
                    this[idx] = AddLockerPhoto(uriString = uri.toString())
                }

                newDataList
            }.onSuccess {
                setState(
                    successState.copy(
                        dataList = it,
                        url = uri.toString(),
                        isRefreshNeed = true
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
