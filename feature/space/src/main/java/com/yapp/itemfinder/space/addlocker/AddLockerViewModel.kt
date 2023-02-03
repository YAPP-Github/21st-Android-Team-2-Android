package com.yapp.itemfinder.space.addlocker

import android.net.Uri
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
    private val lockerApi: LockerApi
) : BaseStateViewModel<AddLockerState, AddLockerSideEffect>() {
    override val _stateFlow: MutableStateFlow<AddLockerState> =
        MutableStateFlow(AddLockerState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<AddLockerSideEffect> = MutableSharedFlow()

    init {
        setState(
            AddLockerState.Success(
                listOf(
                    AddLockerNameInput(),
                    AddLockerSpace(name = "주방"),
                    LockerIcons(),
                    AddLockerPhoto()
                ),
                spaceId = 2L
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

    fun setSpaceId(id: Long) {
        withState<AddLockerState.Success> { state ->
            setState(
                state.copy(spaceId = id)
            )
        }
    }

    fun changeSpace(name: String) {
        withState<AddLockerState.Success> { state ->
            setState(
                state.copy(
                    dataList = ArrayList(state.dataList).apply {
                        removeAt(1)
                        add(1, AddLockerSpace(name = name))
                    }
                )
            )
        }
    }

    fun addNewLocker() = viewModelScope.launch {
        withState<AddLockerState.Success> { state ->
            runCatchingWithErrorHandler {
                lockerApi.addNewLocker(
                    AddLockerRequest(name = "새 보관함", spaceId = 13, icon = "IC_CONTAINER_2")
                )
            }.onSuccess { res ->

            }.onErrorWithResult { e ->

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
                setState(successState.copy(dataList = it))
            }.onErrorWithResult {
                setState(AddLockerState.Error(it))
                val message = it.errorResultEntity.message ?: return@launch
                postSideEffect(AddLockerSideEffect.ShowToast(message))

            }
        }
    }
}
