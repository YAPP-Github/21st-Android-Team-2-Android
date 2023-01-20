package com.yapp.itemfinder.home.tabs.home

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.HomeSpaceRepositoryQualifier
import com.yapp.itemfinder.domain.model.*
import com.yapp.itemfinder.domain.repository.BannerRepository
import com.yapp.itemfinder.domain.repository.HomeSpaceRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import com.yapp.itemfinder.feature.common.utility.DataWithSpan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeTabViewModel @Inject constructor(
    private val bannerMockRepository: BannerRepository,
    @HomeSpaceRepositoryQualifier
    private val homeSpaceRepository: HomeSpaceRepository
) : BaseStateViewModel<HomeTabState, HomeTabSideEffect>() {

    override val _stateFlow: MutableStateFlow<HomeTabState> =
        MutableStateFlow(HomeTabState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<HomeTabSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(HomeTabState.Loading)
        val dataWithSpan = mutableListOf<DataWithSpan<Data>>().apply {
            add(DataWithSpan(MySpaceUpperCellItem("내 공간"),2))
        }
        runCatchingWithErrorHandler {
            homeSpaceRepository.getHomeSpaces()
        }.onSuccess { spaces ->
            setState(
                if (spaces.isEmpty()) {
                    HomeTabState.Empty
                } else {
                    HomeTabState.Success(
                        dataListWithSpan = dataWithSpan.apply {
                            spaces.forEach {
                                add(DataWithSpan(it, 1))
                            }
                            add(DataWithSpan(EmptyCellItem(heightDp = 32),2))
                        }
                    )
                }
            )
        }.onFailure {
            setState(
                HomeTabState.Success(dataWithSpan)
            )
        }
    }

    fun updateCount(item: LikeItem) = viewModelScope.launch {
        // 이런 식으로 분리해서 처리
    }

    fun moveSpaceDetail(space: SpaceItem) {
        postSideEffect(HomeTabSideEffect.MoveSpaceDetail(space))
    }

    fun moveSpaceManagementPage(mySpaceUpperCellItem: MySpaceUpperCellItem) {
        postSideEffect(HomeTabSideEffect.MoveSpacesManage(mySpaceUpperCellItem))
    }

    fun moveLockerDetailPage(locker: LockerEntity){
        postSideEffect(HomeTabSideEffect.MoveLockerDetail(locker))
    }
}
