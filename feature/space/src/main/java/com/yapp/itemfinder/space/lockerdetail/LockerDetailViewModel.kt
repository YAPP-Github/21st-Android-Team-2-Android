package com.yapp.itemfinder.space.lockerdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.LockerRepositoryQualifiers
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.repository.LockerRepository
import com.yapp.itemfinder.domain.repository.ItemRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LockerDetailViewModel @AssistedInject constructor(
    @Assisted private val locker: LockerEntity,
    private val itemRepository: ItemRepository,
    @LockerRepositoryQualifiers private val lockerRepository: LockerRepository,

    ) : BaseStateViewModel<LockerDetailState, LockerDetailSideEffect>() {
    override val _stateFlow: MutableStateFlow<LockerDetailState> =
        MutableStateFlow(LockerDetailState.Uninitialized(locker = locker))

    override val _sideEffectFlow: MutableSharedFlow<LockerDetailSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {

        // api를 붙일 경우, args의 id를 활용하세요
        runCatchingWithErrorHandler {
            setState(LockerDetailState.Loading)
            mutableListOf<Data>().apply {
                add(locker)
                addAll(itemRepository.getItemsByLockerId(locker.id))
            }
        }.onSuccess {
            setState(
                LockerDetailState.Success(
                    locker = it.first() as LockerEntity,
                    dataList = it.drop(1)
                )
            )

        }.onFailure {
            setState(LockerDetailState.Error(it))
        }
    }

    @dagger.assisted.AssistedFactory
    interface LockerIdAssistedFactory {
        fun create(locker: LockerEntity): LockerDetailViewModel
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: LockerIdAssistedFactory,
            locker:LockerEntity
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(locker) as T
            }
        }
    }
}
