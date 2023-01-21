package com.yapp.itemfinder.space.additem

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.domain.model.AddItemCategory
import com.yapp.itemfinder.domain.model.AddItemName
import com.yapp.itemfinder.domain.model.Data
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
                    AddItemCategory(location = "")
                )
            )
        )
    }

    fun setCategory(newCategory: String) {
        var newDataList: List<Data> = listOf()
        withState<AddItemState.Success> { state ->
            newDataList = ArrayList(state.dataList)
            val categoryIndex = newDataList.indexOf(newDataList.find { it is AddItemCategory })
            (newDataList as ArrayList<Data>)[categoryIndex] = (newDataList[categoryIndex] as AddItemCategory).copy(category = newCategory)
        }
        setState(
            AddItemState.Success(
                newDataList
            )
        )
//        withState<AddItemState.Success> { state ->
//            val addItemInfoRequired = state.dataList[0] as AddItemInfoRequired
//            addItemInfoRequired.category = newCategory
//            val newDataList = ArrayList(state.dataList)
//            newDataList[0] = (newDataList[0] as AddItemInfoRequired).copy(category = newCategory)
//            state.copy(
//                dataList = newDataList
//            )
//        }
    }

    fun openSelectCategoryDialog() {
        postSideEffect(AddItemSideEffect.OpenSelectCategoryDialog)
    }
}
