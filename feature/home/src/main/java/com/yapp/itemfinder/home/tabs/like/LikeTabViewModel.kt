package com.yapp.itemfinder.home.tabs.like

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.LikeItem
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LikeTabViewModel : BaseStateViewModel<LikeTabState, LikeTabSideEffect>() {

    override val _stateFlow: MutableStateFlow<LikeTabState>
        get() = MutableStateFlow(LikeTabState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<LikeTabSideEffect>
        get() = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(LikeTabState.Loading)
        setState(
            LikeTabState.Success(
                (0..5).map {
                    LikeItem(
                        id = it.toLong(),
                        name = "즐겨찾기 아이템 ${it}번"
                    )
                }
            )
        )

        val withState = withState<LikeTabState.Success> { state ->
            setState(
                state.copy(
                    dataList = ArrayList(state.dataList).apply {
                        val id = 6L
                        add(
                            LikeItem(
                                id = id,
                                name = "즐찾 아이템 ${id}번"
                            )
                        )
                    }
                )
            )
        }
    }

    fun deleteItem(item: LikeItem): Job = viewModelScope.launch {
        val withState = withState<LikeTabState.Success> { state ->
            setState(
                state.copy(
                    dataList = state.dataList.toMutableList().apply {
                        remove(item)
                    }
                )
            )
            postSideEffect(
                LikeTabSideEffect.ShowToast(
                    "${item}이 삭제됐습니다."
                )
            )
        }
    }

    fun updateCount(item: LikeItem) = viewModelScope.launch {
        val withState = withState<LikeTabState.Success> { state ->
            val dataList = state.dataList
            val foundData = dataList.find { prevData -> prevData.id == item.id } as LikeItem
            val foundIndex = dataList.indexOf(foundData)
            setState(
                state.copy(
                    dataList = dataList.toMutableList().apply {
                        this[foundIndex] = foundData.copy(
                            name = "즐겨찾기 아이템 100번"
                        )
                    }
                )
            )
            postSideEffect(
                LikeTabSideEffect.ShowToast(
                    "${item}이 업데이트 됐습니다."
                )
            )
        }
    }

}
