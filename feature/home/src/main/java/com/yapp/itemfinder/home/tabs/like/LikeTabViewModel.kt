package com.yapp.itemfinder.home.tabs.like

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.CellType
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.LikeItem
import com.yapp.itemfinder.feature.common.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LikeTabViewModel : BaseViewModel() {
    val likeTabStateFlow = MutableStateFlow<LikeTabState>(LikeTabState.Uninitialized)
    val likeTabSideEffect = MutableSharedFlow<LikeTabSideEffect>()

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
        }
    }

    inline fun <reified S: LikeTabState> withState(accessState: (S) -> Unit): Boolean {
        if (likeTabStateFlow.value is S) {
            accessState(likeTabStateFlow.value as S)
            return true
        }
        return false
    }

    fun setState(state: LikeTabState) {
        likeTabStateFlow.value = state
    }

    fun postSideEffect(sideEffect: LikeTabSideEffect) = viewModelScope.launch {
        likeTabSideEffect.emit(sideEffect)
    }

}
