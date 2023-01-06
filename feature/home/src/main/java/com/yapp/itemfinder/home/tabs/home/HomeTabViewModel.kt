package com.yapp.itemfinder.home.tabs.home

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.CellItem
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.LikeItem
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.domain.repository.BannerRepository
import com.yapp.itemfinder.domain.repository.SpaceRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.utility.DataWithSpan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeTabViewModel @Inject constructor(
    private val bannerMockRepository: BannerRepository,
    private val spaceMockRepository: SpaceRepository
) : BaseStateViewModel<HomeTabState, HomeTabSideEffect>(

) {

    override val _stateFlow: MutableStateFlow<HomeTabState> =
        MutableStateFlow(HomeTabState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<HomeTabSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(HomeTabState.Loading)

//        val banners = async { bannerMockRepository.getAllBanner() }.await()
        val spaces = async { spaceMockRepository.getAllSpace() }.await()
        // call API
        setState(
            HomeTabState.Success(
                dataListWithSpan = mutableListOf<DataWithSpan<Data>>().apply {
                    add(DataWithSpan(CellItem(),2))
                    spaces.forEach {
                        add(DataWithSpan(it, 1))
                    }
                }
            )
        )
    }

    fun updateCount(item: LikeItem) = viewModelScope.launch {
        // 이런 식으로 분리해서 처리
    }

    fun goSpaceDetail(space: SpaceItem) {
        postSideEffect(HomeTabSideEffect.MoveSpaceDetail(space))
    }

    fun goSpaceEdit(){
        postSideEffect(HomeTabSideEffect.MoveSpacesManage)
    }
}
