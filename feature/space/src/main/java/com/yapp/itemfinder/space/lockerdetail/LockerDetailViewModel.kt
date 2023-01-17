package com.yapp.itemfinder.space.lockerdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.domain.repository.LockerRepository
import com.yapp.itemfinder.domain.repository.ThingRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LockerDetailViewModel @AssistedInject constructor(
    @Assisted private val lockerId: Long,
    private val thingsRepository: ThingRepository,
    private val lockerRepository: LockerRepository,

    ) : BaseStateViewModel<LockerDetailState, LockerDetailSideEffect>() {
    override val _stateFlow: MutableStateFlow<LockerDetailState> =
        MutableStateFlow(LockerDetailState.Uninitialized(lockerId = lockerId))

    override val _sideEffectFlow: MutableSharedFlow<LockerDetailSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {

        // api를 붙일 경우, args의 id를 활용하세요
        runCatchingWithErrorHandler {
            setState(LockerDetailState.Loading)
            mutableListOf<Data>().apply {
                add(lockerRepository.getLocker(lockerId))
                addAll(thingsRepository.getThingsByLockerId(lockerId))
            }
        }.onSuccess {
            setState(
                LockerDetailState.Success(
                    locker = it.first() as Locker,
                    dataList = it.drop(1)
                )
            )

        }.onFailure {
            setState(LockerDetailState.Error(it))
        }
    }

    @dagger.assisted.AssistedFactory
    interface LockerIdAssistedFactory {
        fun create(lockerId: Long): LockerDetailViewModel
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: LockerIdAssistedFactory,
            lockerId: Long
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(lockerId) as T
            }
        }
    }
}
