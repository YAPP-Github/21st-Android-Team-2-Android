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
                    AddItemCategory(category = ItemCategorySelection.DEFAULT.label),
                    AddItemLocation(),
                    AddItemCount(),
                    AddItemTags(listOf()),
                    AddItemAdditional()
                )
            )
        )
    }

    fun setName(newName: String) {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val nameIndex = newDataList.indexOf(newDataList.find { it is AddItemName })
            newDataList[nameIndex] =
                (newDataList[nameIndex] as AddItemName).copy(name = newName)
            setState(
                AddItemState.Success(
                    newDataList
                )
            )
        }
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

    fun addMemoCell() {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemAdditional =
                newDataList.find { it is AddItemAdditional } as AddItemAdditional
            val idx = newDataList.indexOf(addItemAdditional)
            newDataList.add(idx, AddItemMemo())
            newDataList[idx + 1] = addItemAdditional.copy(hasMemo = true)
            setState(
                AddItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun addExpirationDateCell() {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemAdditional =
                newDataList.find { it is AddItemAdditional } as AddItemAdditional
            val idx = newDataList.indexOf(addItemAdditional)
            newDataList.add(idx, AddItemExpirationDate())
            newDataList[idx + 1] = addItemAdditional.copy(hasExpirationDate = true)
            setState(
                AddItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun addPurchaseDateCell() {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemAdditional =
                newDataList.find { it is AddItemAdditional } as AddItemAdditional
            val idx = newDataList.indexOf(addItemAdditional)
            newDataList.add(idx, AddItemPurchaseDate())
            newDataList[idx + 1] = addItemAdditional.copy(hasPurchaseDate = true)
            setState(
                AddItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun setMemo(newMemo: String) {

    }

    fun setExpirationDate(date: String) {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val idx = newDataList.indexOf(newDataList.find { it is AddItemExpirationDate })
            newDataList[idx] = AddItemExpirationDate(expirationDate = date)
            setState(
                AddItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun setPurchaseDate(date: String) {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val idx = newDataList.indexOf(newDataList.find { it is AddItemPurchaseDate })
            newDataList[idx] = AddItemPurchaseDate(purchaseDate = date)
            setState(
                AddItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun saveItem() {
        withState<AddItemState.Success> { state ->
            val dataList = state.dataList
            var itemName = ""
            var itemCategory = ""
            var itemSpace = ""
            var itemLocker = ""
            dataList.forEach {
                if (it is AddItemName) itemName = it.name
                if (it is AddItemCategory) itemCategory = it.category
                if (it is AddItemLocation) {
                    itemSpace = it.spaceName
                    itemLocker = it.lockerName
                }
            }
            if (itemName == null && itemCategory == null && itemSpace == null && itemLocker == null) {
                postSideEffect(AddItemSideEffect.FillOutRequiredToast)
                return
            }
            if (itemName == null) {
                postSideEffect(AddItemSideEffect.FillOutNameToast)
                return
            }
            if (itemCategory == null) {
                postSideEffect(AddItemSideEffect.FillOutCategoryToast)
                return
            }
            if (itemSpace == null) {
                postSideEffect(AddItemSideEffect.FillOutLocationToast)
                return
            }
        }
    }

    fun openExpirationDatePicker() {
        postSideEffect(AddItemSideEffect.OpenExpirationDatePicker)
    }

    fun openPurchaseDatePicker() {
        postSideEffect(AddItemSideEffect.OpenPurchaseDatePicker)
    }

    fun openSelectCategoryDialog() {
        postSideEffect(AddItemSideEffect.OpenSelectCategoryDialog)
    }
}
