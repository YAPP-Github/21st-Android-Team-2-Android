package com.yapp.itemfinder.space.additem.selectlocker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.LockerRepositoryQualifiers
import com.yapp.itemfinder.domain.model.SelectLockerEntity
import com.yapp.itemfinder.domain.model.SpaceAndLockerEntity
import com.yapp.itemfinder.domain.repository.LockerRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.onErrorWithResult
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import com.yapp.itemfinder.space.additem.AddItemActivity.Companion.SELECTED_SPACE_AND_LOCKER_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemSelectLockerViewModel @Inject constructor(
    @LockerRepositoryQualifiers
    private val lockerRepository: LockerRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseStateViewModel<AddItemSelectLockerState, AddItemSelectLockerSideEffect>() {

    override val _stateFlow: MutableStateFlow<AddItemSelectLockerState> =
        MutableStateFlow(AddItemSelectLockerState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<AddItemSelectLockerSideEffect> = MutableSharedFlow()

    private val selectedSpaceAndLockerEntity by lazy {
        savedStateHandle.get<SpaceAndLockerEntity>(SELECTED_SPACE_AND_LOCKER_KEY)!!
    }

    override fun fetchData(): Job = viewModelScope.launch {
        runCatchingWithErrorHandler {
            setState(AddItemSelectLockerState.Loading)
            val (selectedSpace, selectedLocker) = selectedSpaceAndLockerEntity
            lockerRepository.getLockers(selectedSpace.id) to selectedLocker
        }.onSuccess { (lockers, selectedLocker) ->
            setState(
                AddItemSelectLockerState.Success(
                    dataList = lockers.map {
                        val isSelected = it.id == selectedLocker?.id
                        SelectLockerEntity.fromLockerEntity(it, isSelected)
                    }
                )
            )
        }.onErrorWithResult {
            setState(AddItemSelectLockerState.Error(it))
            val message = it.errorResultEntity.message ?: return@launch
            postSideEffect(AddItemSelectLockerSideEffect.ShowToast(message))
        }
    }

    fun selectLocker(selectLockerEntity: SelectLockerEntity) {
        withState<AddItemSelectLockerState.Success> { state ->
            setState(
                state.copy(
                    dataList = state.dataList.toMutableList().map {
                        (it as SelectLockerEntity).copy(
                            isSelected = it.id == selectLockerEntity.id
                        )
                    }
                )
            )
        }
        savePath()
    }

    private fun savePath() {
        withState<AddItemSelectLockerState.Success> { state ->
            val lockerEntity = state.dataList
                .filterIsInstance<SelectLockerEntity>()
                .firstOrNull { it.isSelected }
                ?.toLockerEntity()

            postSideEffect(
                AddItemSelectLockerSideEffect.SavePath(
                    spaceAndLockerEntity = selectedSpaceAndLockerEntity.copy(
                        lockerEntity = lockerEntity
                    )
                )
            )
        }
    }

}
