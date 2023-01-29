package com.yapp.itemfinder.space.edititem

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
class EditItemViewModel @Inject constructor(

) : BaseStateViewModel<EditItemState, EditItemSideEffect>() {
    override val _stateFlow: MutableStateFlow<EditItemState> =
        MutableStateFlow(EditItemState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<EditItemSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(EditItemState.Loading)
        // api call or 이전 페이지에서 전달
        val sampleItem = Item(
            id = 1,
            lockerId = 1,
            itemCategory = ItemCategory.FOOD,
            name = "선크림",
            expirationDate = "2022.12.25.",
            purchaseDate = null,
            memo = null,
            imageUrl = "http://source.unsplash.com/random/150x150",
            tags = listOf(Tag("생활"), Tag("화장품")),
            count = 1
        )
        val dataList = mutableListOf<Data>(
            AddItemName(name = sampleItem.name, mode = ScreenMode.EDIT_MODE),
            AddItemCategory(category = ItemCategorySelection.FOOD),
            AddItemLocation(
                spaceName = "주방",
                spaceId = 111,
                lockerName = "냉장고",
                lockerId = 222
            ),
            AddItemCount(count = sampleItem.count)
        ).apply {
            sampleItem.tags?.let { add(AddItemTags(it)) }
            sampleItem.memo?.let {
                add(
                    AddItemMemo(
                        memo = sampleItem.memo!!,
                        mode = ScreenMode.EDIT_MODE
                    )
                )
            }
            sampleItem.expirationDate?.let { add(AddItemExpirationDate(it)) }
            sampleItem.purchaseDate?.let { add(AddItemPurchaseDate(it)) }
            add(
                AddItemAdditional(
                    hasMemo = (sampleItem.memo != null),
                    hasExpirationDate = sampleItem.expirationDate != null,
                    hasPurchaseDate = sampleItem.purchaseDate != null
                )
            )
        }
        setState(
            EditItemState.Success(
                dataList = dataList
            )
        )
    }

    fun setName(newName: String) {
        withState<EditItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val nameIndex = newDataList.indexOf(newDataList.find { it is AddItemName })
            newDataList[nameIndex] =
                (newDataList[nameIndex] as AddItemName).copy(name = newName)
            setState(
                EditItemState.Success(
                    newDataList,
                    isRefreshNeed = false
                )
            )
        }
    }

    fun setCategory(newCategory: ItemCategorySelection) {
        withState<EditItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val categoryIndex = newDataList.indexOf(newDataList.find { it is AddItemCategory })
            newDataList[categoryIndex] =
                (newDataList[categoryIndex] as AddItemCategory).copy(category = newCategory)
            setState(
                EditItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun countPlusOne() {
        withState<EditItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemCountItem = newDataList.find { it is AddItemCount } as AddItemCount
            val countIndex = newDataList.indexOf(addItemCountItem)
            newDataList[countIndex] = addItemCountItem.copy(
                count = addItemCountItem.count.plus(1)
            )
            setState(
                EditItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun countMinusOne() {
        withState<EditItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemCountItem = newDataList.find { it is AddItemCount } as AddItemCount
            val countIndex = newDataList.indexOf(addItemCountItem)
            newDataList[countIndex] = addItemCountItem.copy(
                count = addItemCountItem.count.minus(1)
            )
            setState(
                EditItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun addMemoCell() {
        withState<EditItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemAdditional =
                newDataList.find { it is AddItemAdditional } as AddItemAdditional
            val idx = newDataList.indexOf(addItemAdditional)
            newDataList.add(idx, AddItemMemo(mode = ScreenMode.ADD_MODE))
            newDataList[idx + 1] = addItemAdditional.copy(hasMemo = true)
            setState(
                EditItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun addExpirationDateCell() {
        withState<EditItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemAdditional =
                newDataList.find { it is AddItemAdditional } as AddItemAdditional
            val idx = newDataList.indexOf(addItemAdditional)
            newDataList.add(idx, AddItemExpirationDate())
            newDataList[idx + 1] = addItemAdditional.copy(hasExpirationDate = true)
            setState(
                EditItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun addPurchaseDateCell() {
        withState<EditItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemAdditional =
                newDataList.find { it is AddItemAdditional } as AddItemAdditional
            val idx = newDataList.indexOf(addItemAdditional)
            newDataList.add(idx, AddItemPurchaseDate())
            newDataList[idx + 1] = addItemAdditional.copy(hasPurchaseDate = true)
            setState(
                EditItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun setMemo(newMemo: String) {
        withState<EditItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val memoIndex = newDataList.indexOf(newDataList.find { it is AddItemMemo })
            newDataList[memoIndex] =
                (newDataList[memoIndex] as AddItemMemo).copy(memo = newMemo)
            setState(
                EditItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun setExpirationDate(date: String) {
        withState<EditItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val idx = newDataList.indexOf(newDataList.find { it is AddItemExpirationDate })
            newDataList[idx] = AddItemExpirationDate(expirationDate = date)
            setState(
                EditItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun setPurchaseDate(date: String) {
        withState<EditItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val idx = newDataList.indexOf(newDataList.find { it is AddItemPurchaseDate })
            newDataList[idx] = AddItemPurchaseDate(purchaseDate = date)
            setState(
                EditItemState.Success(
                    newDataList
                )
            )
        }
    }

    fun saveItem() {
        withState<EditItemState.Success> { state ->
            val dataList = state.dataList
            var itemName = ""
            var itemCategory = ""
            var itemSpace = ""
            var itemLocker = ""
            var itemCount = 1
            var itemMemo = ""
            var itemExpiration = ""
            var itemPurchase = ""
            dataList.forEach {
                if (it is AddItemName) itemName = it.name
                if (it is AddItemCategory) itemCategory = it.category.label
                if (it is AddItemLocation) {
                    itemSpace = it.spaceName
                    itemLocker = it.lockerName
                }
                if (it is AddItemCount) itemCount = it.count
                if (it is AddItemMemo) itemMemo = it.memo
                if (it is AddItemExpirationDate) itemExpiration = it.expirationDate
                if (it is AddItemPurchaseDate) itemPurchase = it.purchaseDate
            }
            if (itemName == "" && itemCategory == ItemCategorySelection.DEFAULT.label && itemSpace == "" && itemLocker == "") {
                postSideEffect(EditItemSideEffect.FillOutRequiredSnackBar)
                return
            }
            if (itemName == "") {
                postSideEffect(EditItemSideEffect.FillOutNameSnackBar)
                return
            }
            if (itemCategory == ItemCategorySelection.DEFAULT.label) {
                postSideEffect(EditItemSideEffect.FillOutCategorySnackBar)
                return
            }
            if (itemSpace == "") {
                postSideEffect(EditItemSideEffect.FillOutLocationSnackBar)
                return
            }
            if (itemName.length > 30) {
                postSideEffect(EditItemSideEffect.NameLengthLimitSnackBar)
                return
            }
            if (itemCategory.length > 200) {
                postSideEffect(EditItemSideEffect.MemoLengthLimitSnackBar)
                return
            }
            // save
        }
    }

    fun openExpirationDatePicker() {
        postSideEffect(EditItemSideEffect.OpenExpirationDatePicker)
    }

    fun openPurchaseDatePicker() {
        postSideEffect(EditItemSideEffect.OpenPurchaseDatePicker)
    }

    fun openSelectCategoryDialog() {
        postSideEffect(EditItemSideEffect.OpenSelectCategoryDialog)
    }
}
