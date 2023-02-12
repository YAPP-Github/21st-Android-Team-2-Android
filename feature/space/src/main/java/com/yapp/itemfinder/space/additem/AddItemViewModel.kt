package com.yapp.itemfinder.space.additem

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.ItemRepositoryQualifiers
import com.yapp.itemfinder.domain.model.*
import com.yapp.itemfinder.domain.repository.ItemRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import com.yapp.itemfinder.space.itemdetail.ItemDetailFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ItemRepositoryQualifiers
    private val itemRepository: ItemRepository,
) : BaseStateViewModel<AddItemState, AddItemSideEffect>() {
    override val _stateFlow: MutableStateFlow<AddItemState> =
        MutableStateFlow(AddItemState.Uninitialized)
    override val _sideEffectFlow: MutableSharedFlow<AddItemSideEffect> = MutableSharedFlow()

    override fun fetchData(): Job = viewModelScope.launch {
        setState(AddItemState.Loading)
        val screenMode = savedStateHandle.get<String>(AddItemActivity.SCREEN_MODE)
        when (screenMode) {
            ScreenMode.ADD_MODE.label -> {
                setState(
                    AddItemState.Success(
                        dataList = listOf(
                            AddItemImages(mutableListOf()),
                            AddItemName(mode = ScreenMode.ADD_MODE),
                            AddItemCategory(category = ItemCategorySelection.DEFAULT),
                            AddItemLocation(),
                            AddItemCount(),
                            AddItemTags(listOf()),
                            AddItemAdditional()
                        ),
                        spaceAndLockerEntity = null,
                    )
                )
            }
            ScreenMode.EDIT_MODE.label -> {
                val itemId = savedStateHandle.get<Long>(ItemDetailFragment.ITEM_ID_KEY)
                runCatchingWithErrorHandler {
                    // itemId로 api call
                }
                // api 연결하면서 sampleItem 제거
                val sampleItem = Item(
                    id = 1,
                    lockerId = 1,
                    itemCategory = ItemCategory.FOOD,
                    name = "선크림",
                    expirationDate = "2022.12.25.",
                    purchaseDate = null,
                    memo = null,
                    imageUrls = listOf("http://source.unsplash.com/random/150x150"),
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
                    AddItemState.Success(
                        dataList = dataList
                    )
                )
            }
            else -> {}
        }
    }

    fun startChooseImages() {
        withState<AddItemState.Success> { state ->
            val idx = state.dataList.indexOfFirst { data -> data is AddItemImages }
            postSideEffect(AddItemSideEffect.OpenPhotoPicker(state.dataList[idx] as AddItemImages))
        }

    }

    // 이미지 피커에서 이미지를 선택한 다음, 하나의 이미지의 삭제 버튼을 눌렀을 경우 호출합니다.
    fun cancelImageUpload(uriStringList: List<String>) {
        withState<AddItemState.Success> { state ->
            val newDataList = state.dataList.toMutableList()
            val imageIndex = newDataList.indexOfFirst { data -> data is AddItemImages }
            newDataList[imageIndex] = (state.dataList[imageIndex] as AddItemImages).copy(
                uriStringList = uriStringList
            )
            setState(AddItemState.Success(newDataList))

        }

    }


    fun doneChooseImages(uris: List<Uri>) {
        withState<AddItemState.Success> { state ->
            val newDataList = state.dataList.toMutableList()
            val imageIndex = newDataList.indexOfFirst { data -> data is AddItemImages }
            newDataList[imageIndex] = (state.dataList[imageIndex] as AddItemImages).copy(
                uriStringList = uris.map { it.toString() }
            )
            setState(
                AddItemState.Success(
                    newDataList
                )
            )
        }

    }

    fun setName(newName: String) {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val nameIndex = newDataList.indexOf(newDataList.find { it is AddItemName })
            newDataList[nameIndex] =
                (newDataList[nameIndex] as AddItemName).copy(name = newName)
            setState(
                AddItemState.Success(
                    newDataList,
                    isRefreshNeed = false
                )
            )
        }
    }

    fun setCategory(newCategory: ItemCategorySelection) {
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
            newDataList.add(idx, AddItemMemo(mode = ScreenMode.ADD_MODE))
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
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val memoIndex = newDataList.indexOf(newDataList.find { it is AddItemMemo })
            newDataList[memoIndex] =
                (newDataList[memoIndex] as AddItemMemo).copy(memo = newMemo)
            setState(
                AddItemState.Success(
                    newDataList
                )
            )
        }
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

    fun setSelectedSpaceAndLocker(spaceAndLockerEntity: SpaceAndLockerEntity) {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val idx = newDataList.indexOf(newDataList.find { it is AddItemLocation })
            val (space, locker) = spaceAndLockerEntity
            newDataList[idx] = AddItemLocation(
                spaceId = space.id,
                spaceName = space.name,
                lockerId = locker?.id ?: 0L,
                lockerName = locker?.name ?: ""
            )
            setState(
                state.copy(
                    dataList = newDataList,
                    spaceAndLockerEntity = spaceAndLockerEntity
                )
            )
        }
    }

    fun saveItem() {
        withState<AddItemState.Success> { state ->
            state.dataList.filterIsInstance<AddItemName>().firstOrNull()?.saveName()
            state.dataList.filterIsInstance<AddItemMemo>().firstOrNull()?.saveMemo()
        }
        withState<AddItemState.Success> { state ->
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
                postSideEffect(AddItemSideEffect.FillOutRequiredSnackBar)
                return
            }
            if (itemName == "") {
                postSideEffect(AddItemSideEffect.FillOutNameSnackBar)
                return
            }
            if (itemCategory == ItemCategorySelection.DEFAULT.label) {
                postSideEffect(AddItemSideEffect.FillOutCategorySnackBar)
                return
            }
            if (itemSpace == "") {
                postSideEffect(AddItemSideEffect.FillOutLocationSnackBar)
                return
            }
            if (itemName.length > 30) {
                postSideEffect(AddItemSideEffect.NameLengthLimitSnackBar)
                return
            }
            if (itemMemo.length > 200) {
                postSideEffect(AddItemSideEffect.MemoLengthLimitSnackBar)
                return
            }
            // save
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

    fun moveSelectSpace() {
        withState<AddItemState.Success> { state ->
            postSideEffect(
                AddItemSideEffect.MoveSelectSpace(
                    spaceAndLockerEntity = state.spaceAndLockerEntity
                )
            )
        }
    }

}
