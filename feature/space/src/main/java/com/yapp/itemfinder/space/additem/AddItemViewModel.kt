package com.yapp.itemfinder.space.additem

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.*
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(

) : BaseStateViewModel<AddItemState, AddItemSideEffect>() {
    override val _stateFlow: MutableStateFlow<AddItemState> =
        MutableStateFlow(AddItemState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<AddItemSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(AddItemState.Loading)
        setState(
            AddItemState.Success(
                dataList = listOf(
                    AddItemName(),
                    AddItemCategory(location = ""),
                    AddItemLocation(),
                    AddItemCount(),
                    AddItemTags(listOf())
                )
            )
        )
    }

    fun setCategory(newCategory: String) {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val categoryIndex = newDataList.indexOf(newDataList.find { it is AddItemCategory })
            newDataList[categoryIndex] =
                (newDataList[categoryIndex] as AddItemCategory).copy(category = newCategory)
            setState(
                AddItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun countPlusOne() {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemCountItem = newDataList.find { it is AddItemCount } as AddItemCount
            val countIndex = newDataList.indexOf(addItemCountItem)
            newDataList[countIndex] = addItemCountItem.copy(
                count = addItemCountItem.count.plus(1)
            )
            setState(
                AddItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun countMinusOne() {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemCountItem = newDataList.find { it is AddItemCount } as AddItemCount
            val countIndex = newDataList.indexOf(addItemCountItem)
            newDataList[countIndex] = addItemCountItem.copy(
                count = addItemCountItem.count.minus(1)
            )
            setState(
                AddItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun openSelectCategoryDialog() {
        postSideEffect(AddItemSideEffect.OpenSelectCategoryDialog)
    }
}
