package com.yapp.itemfinder.space.additem

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.data.repositories.di.ItemRepositoryQualifiers
import com.yapp.itemfinder.domain.model.*
import com.yapp.itemfinder.domain.repository.ImageRepository
import com.yapp.itemfinder.domain.repository.ItemRepository
import com.yapp.itemfinder.feature.common.BaseStateViewModel
import com.yapp.itemfinder.feature.common.extension.cropToJpeg
import com.yapp.itemfinder.feature.common.extension.onErrorWithResult
import com.yapp.itemfinder.feature.common.extension.runCatchingWithErrorHandler
import com.yapp.itemfinder.space.addlocker.AddLockerSideEffect
import com.yapp.itemfinder.space.addlocker.AddLockerState
import com.yapp.itemfinder.space.itemdetail.ItemDetailFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    @ItemRepositoryQualifiers
    private val itemRepository: ItemRepository,
    private val imageRepository: ImageRepository
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
            setState(
                state.copy(
                    dataList = newDataList
                )
            )
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
                state.copy(
                    dataList = newDataList
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
                state.copy(
                    dataList = newDataList
                )
            )
        }
    }

    fun setCategory(newCategory: ItemCategorySelection) {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val categoryIndex = newDataList.indexOf(newDataList.find { it is AddItemCategory })

            val addItemMarkerMap = newDataList.find { it is AddItemMarkerMap } as? AddItemMarkerMap
            val itemCategory = newCategory.toItemCateogry()
            newDataList[categoryIndex] =
                (newDataList[categoryIndex] as AddItemCategory).copy(category = newCategory)


            addItemMarkerMap?.runItemCategorySet(itemCategory)
            val addItemMarkerMapIndex = newDataList.indexOf(addItemMarkerMap)
            if (addItemMarkerMapIndex > -1) {
                newDataList[addItemMarkerMapIndex] = addItemMarkerMap?.copy(
                    item = addItemMarkerMap.item?.copy(itemCategory = itemCategory)
                )
            }
            setState(
                state.copy(
                    dataList = newDataList,
                    lockerAndItemEntity = state.lockerAndItemEntity?.copy(
                        item = state.lockerAndItemEntity.item?.copy(
                            itemCategory = itemCategory
                        )
                    )
                )
            )
        }
    }

    fun getSelectedCategory(): ItemCategorySelection {
        var category = ItemCategorySelection.DEFAULT
        withState<AddItemState.Success> { state ->
            category = state.dataList.filterIsInstance<AddItemCategory>().firstOrNull()?.category
                ?: ItemCategorySelection.DEFAULT
        }
        return category
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
                state.copy(
                    dataList = newDataList,
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
                state.copy(
                    dataList = newDataList,
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
                state.copy(
                    dataList = newDataList,
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
                state.copy(
                    dataList = newDataList,
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
                state.copy(
                    dataList = newDataList,
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
                state.copy(
                    dataList = newDataList,
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
                state.copy(
                    dataList = newDataList,
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
                state.copy(
                    dataList = newDataList,
                )
            )
        }
    }

    fun setSelectedSpaceAndLocker(spaceAndLockerEntity: SpaceAndLockerEntity) {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemLocationIdx = newDataList.indexOf(newDataList.find { it is AddItemLocation })
            val (space, locker) = spaceAndLockerEntity
            newDataList[addItemLocationIdx] = AddItemLocation(
                spaceId = space.id,
                spaceName = space.name,
                lockerId = locker?.id ?: 0L,
                lockerName = locker?.name ?: ""
            )

            val addItemCategory = newDataList.find { it is AddItemCategory } as AddItemCategory

            val markerMap = newDataList.find { it is AddItemMarkerMap } as? AddItemMarkerMap
            markerMap?.let { newDataList.remove(it) }
            locker?.let {
                val newItem = state.lockerAndItemEntity?.item?.copy(
                    itemCategory = addItemCategory.category.toItemCateogry()
                )

                newDataList.add(
                    AddItemMarkerMap(
                        lockerEntity = locker,
                        item = newItem
                    )
                )
                setState(
                    state.copy(
                        dataList = newDataList,
                        spaceAndLockerEntity = spaceAndLockerEntity,
                        lockerAndItemEntity = state.lockerAndItemEntity?.copy(
                            lockerEntity = locker,
                            item = newItem
                        )
                    )
                )
            } ?: run {
                setState(
                    state.copy(
                        dataList = newDataList,
                        spaceAndLockerEntity = spaceAndLockerEntity,
                    )
                )
            }
        }
    }

    fun setDefinedLockerAndItem(lockerAndItemEntity: LockerAndItemEntity) {
        withState<AddItemState.Success> { state ->
            val newDataList = ArrayList(state.dataList)
            val addItemMarkerMap = newDataList.find { it is AddItemMarkerMap } as? AddItemMarkerMap

            val addItemCategory = newDataList.find { it is AddItemCategory } as AddItemCategory
            val itemCategory = addItemCategory.category.toItemCateogry()

            addItemMarkerMap?.let {
                val idx = newDataList.indexOf(addItemMarkerMap)
                val (_, item) = lockerAndItemEntity
                newDataList[idx] = addItemMarkerMap.copy(
                    item = item?.copy(
                        itemCategory = itemCategory
                    )
                )
            }
            setState(
                state.copy(
                    dataList = newDataList,
                    lockerAndItemEntity = lockerAndItemEntity
                )
            )
        }
    }

    fun saveItem() = viewModelScope.launch{
        withState<AddItemState.Success> { state ->
            state.dataList.filterIsInstance<AddItemName>().firstOrNull()?.saveName()
            state.dataList.filterIsInstance<AddItemMemo>().firstOrNull()?.saveMemo()
        }
        withState<AddItemState.Success> { state ->
            val dataList = state.dataList
            var itemName = ""
            var itemCategorySelection: ItemCategorySelection? = null
            var itemSpace = ""
            var itemLockerName = ""
            var itemLockerId = -1L
            var itemCount = 1
            var itemMemo = ""
            var itemExpiration = ""
            var itemPurchase = ""
            var tagIds = listOf<Long>()
            var imageUriStringList = listOf<String>()
            dataList.forEach {
                if (it is AddItemName) itemName = it.name
                else if (it is AddItemCategory) itemCategorySelection = it.category
                else if (it is AddItemLocation) {
                    itemSpace = it.spaceName
                    itemLockerName = it.lockerName
                    itemLockerId = it.lockerId
                }
                else if (it is AddItemCount) itemCount = it.count
                else if (it is AddItemMemo) itemMemo = it.memo
                else if (it is AddItemExpirationDate) itemExpiration = it.expirationDate
                else if (it is AddItemPurchaseDate) itemPurchase = it.purchaseDate
                else if ( it is AddItemImages){
                    imageUriStringList = it.uriStringList
                }
            }
            if (itemName == "" && itemCategorySelection == ItemCategorySelection.DEFAULT && itemSpace == "" && itemLockerName == "") {
                postSideEffect(AddItemSideEffect.FillOutRequiredSnackBar)
                return@withState
            }
            if (itemName == "") {
                postSideEffect(AddItemSideEffect.FillOutNameSnackBar)
                return@withState
            }
            if (itemCategorySelection == ItemCategorySelection.DEFAULT) {
                postSideEffect(AddItemSideEffect.FillOutCategorySnackBar)
                return@withState
            }
            if (itemSpace == "") {
                postSideEffect(AddItemSideEffect.FillOutLocationSnackBar)
                return@withState
            }
            if (itemName.length > 30) {
                postSideEffect(AddItemSideEffect.NameLengthLimitSnackBar)
                return@withState
            }
            if (itemMemo.length > 200) {
                postSideEffect(AddItemSideEffect.MemoLengthLimitSnackBar)
                return@withState
            }
            // save


            runCatchingWithErrorHandler {
                var imageUrls = listOf<String>()
                if (imageUriStringList.isNotEmpty()){
                    val imagePaths = withContext(Dispatchers.IO){
                        imageUriStringList.map { it.toUri().cropToJpeg(context,1,1) }
                    }
                    imageUrls = imageRepository.addImages(imagePaths)
                }

                itemRepository.addItem(
                    containerId = itemLockerId,
                    name = itemName,
                    itemType = itemCategorySelection.toString(),
                    quantity = itemCount,
                    imageUrls = imageUrls,
                    tagIds = tagIds,
                    description = itemMemo,
                    purchaseDate = itemPurchase.replace(".","-"),
                    // UTC 시간대의 오전 0시 (한국시간 기준으로 09시) 추후 UI 변경사항 고려해서 값 대입할 것.
                    useByDate = itemExpiration.replace(".","-") + "T00:00:00.000Z",
                )
            }.onSuccess {
                postSideEffect(AddItemSideEffect.ShowToast("추가성공"))
                postSideEffect(AddItemSideEffect.AddItemFinished)
            }.onErrorWithResult { errorWithResult ->
                val message = errorWithResult.errorResultEntity.message
                message?.let { postSideEffect(AddItemSideEffect.ShowToast(it)) }
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

    fun moveSelectSpace() {
        withState<AddItemState.Success> { state ->
            postSideEffect(
                AddItemSideEffect.MoveSelectSpace(
                    spaceAndLockerEntity = state.spaceAndLockerEntity
                )
            )
        }
    }

    fun moveItemPositionDefine() {
        withState<AddItemState.Success> { state ->
            val addItemCategory = state.dataList.find { it is AddItemCategory } as AddItemCategory
            val itemCategory = addItemCategory.category.toItemCateogry()
            state.lockerAndItemEntity?.let { lockerAndItemEntity ->
                postSideEffect(
                    AddItemSideEffect.MoveItemPositionDefine(
                        lockerAndItemEntity = if (lockerAndItemEntity.item == null) {
                            lockerAndItemEntity.copy(
                                item = Item.createEmptyItem().copy(
                                    itemCategory = itemCategory
                                )
                            )
                        } else {
                            lockerAndItemEntity
                        }
                    )
                )
            } ?: state.spaceAndLockerEntity?.lockerEntity?.let { lockerEntity ->
                postSideEffect(
                    AddItemSideEffect.MoveItemPositionDefine(
                        lockerAndItemEntity = LockerAndItemEntity(
                            lockerEntity = lockerEntity,
                            item = Item.createEmptyItem().copy(
                                itemCategory = itemCategory
                            )
                        )
                    )
                )
            }
        }
    }

}
