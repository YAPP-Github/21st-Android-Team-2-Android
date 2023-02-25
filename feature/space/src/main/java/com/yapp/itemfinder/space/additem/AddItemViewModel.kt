package com.yapp.itemfinder.space.additem

import android.content.Context
import android.net.Uri
import android.webkit.URLUtil
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
import com.yapp.itemfinder.space.itemdetail.ItemDetailFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val screenMode by lazy { savedStateHandle.get<String>(AddItemActivity.SCREEN_MODE) }
    private val itemId by lazy { savedStateHandle.get<Long>(AddItemActivity.ITEM_ID_KEY) }

    override fun fetchData(): Job = viewModelScope.launch {
        setState(AddItemState.Loading)
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
                        spaceAndLockerEntity = null
                    )
                )
                if (savedStateHandle.get<SpaceAndLockerEntity>(AddItemActivity.SELECTED_SPACE_AND_LOCKER_KEY) != null) {
                    val spaceAndLockerEntity =
                        savedStateHandle.get<SpaceAndLockerEntity>(AddItemActivity.SELECTED_SPACE_AND_LOCKER_KEY)!!
                    setSelectedSpaceAndLocker(spaceAndLockerEntity)
                }
            }
            ScreenMode.EDIT_MODE.label -> {
                val spaceAndLockerEntity =
                    savedStateHandle.get<SpaceAndLockerEntity>(AddItemActivity.SELECTED_SPACE_AND_LOCKER_KEY)
                savedStateHandle.get<Long>(ItemDetailFragment.ITEM_ID_KEY)?.let { itemId ->
                    runCatchingWithErrorHandler {
                        itemRepository.getItemById(itemId)
                    }.onSuccess { item ->
                        val addItemLocation = spaceAndLockerEntity?.let { (space, locker) ->
                            AddItemLocation(
                                spaceName = space.name,
                                spaceId = space.id,
                                lockerName = locker?.name ?: "",
                                lockerId = locker?.id ?: 0
                            )
                        } ?: kotlin.run {
                            postSideEffect(AddItemSideEffect.AddItemFinished)
                            return@launch
                        }
                        val dataList = mutableListOf<Data>(
                            AddItemImages(item.imageUrls ?: mutableListOf()),
                            AddItemName(name = item.name, mode = ScreenMode.EDIT_MODE),
                            AddItemCategory(
                                category = item.itemCategory?.toItemCateogrySelection()
                                    ?: ItemCategorySelection.DEFAULT
                            ),
                            addItemLocation,
                            AddItemCount(count = item.count)
                        ).apply {

                            item.tags?.let { add(AddItemTags(it)) }
                            item.memo?.let {
                                add(
                                    AddItemMemo(
                                        memo = item.memo!!,
                                        mode = ScreenMode.EDIT_MODE
                                    )
                                )
                            }
                            item.expirationDate?.let { add(AddItemExpirationDate(it)) }
                            item.purchaseDate?.let { add(AddItemPurchaseDate(it)) }
                            add(
                                AddItemAdditional(
                                    hasMemo = (item.memo != null),
                                    hasExpirationDate = item.expirationDate != null,
                                    hasPurchaseDate = item.purchaseDate != null
                                )
                            )
                        }
                        setState(
                            AddItemState.Success(
                                dataList = dataList
                            )
                        )
                        setSelectedSpaceAndLocker(spaceAndLockerEntity)
                        setDefinedLockerAndItem(
                            LockerAndItemEntity(
                                lockerEntity = requireNotNull(spaceAndLockerEntity.lockerEntity),
                                item = item
                            )
                        )
                    }.onFailure {
                        setState(AddItemState.Error(it))
                    }
                } ?: postSideEffect(AddItemSideEffect.AddItemFinished)
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

                spaceAndLockerEntity.lockerEntity?.imageUrl?.let {
                    newDataList.add(
                        AddItemMarkerMap(
                            lockerEntity = locker,
                            item = newItem
                        )
                    )
                }

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

    fun saveItem() = viewModelScope.launch {
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
            var position = state.lockerAndItemEntity?.item?.position
            dataList.forEach {
                if (it is AddItemName) itemName = it.name
                else if (it is AddItemCategory) itemCategorySelection = it.category
                else if (it is AddItemLocation) {
                    itemSpace = it.spaceName
                    itemLockerName = it.lockerName
                    itemLockerId = it.lockerId
                } else if (it is AddItemCount) itemCount = it.count
                else if (it is AddItemMemo) itemMemo = it.memo
                else if (it is AddItemExpirationDate) itemExpiration = it.expirationDate
                else if (it is AddItemPurchaseDate) itemPurchase = it.purchaseDate
                else if (it is AddItemImages) {
                    imageUriStringList = it.uriStringList
                }
            }
            if (itemName.isBlank() && itemCategorySelection == ItemCategorySelection.DEFAULT && itemSpace.isBlank() && itemLockerName.isBlank()) {
                postSideEffect(AddItemSideEffect.FillOutRequiredSnackBar)
                return@withState
            }
            if (itemName.isBlank()) {
                postSideEffect(AddItemSideEffect.FillOutNameSnackBar)
                return@withState
            }
            if (itemCategorySelection == ItemCategorySelection.DEFAULT) {
                postSideEffect(AddItemSideEffect.FillOutCategorySnackBar)
                return@withState
            }
            if (itemSpace.isBlank()) {
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
                if (imageUriStringList.isNotEmpty()) {
                    if (screenMode == ScreenMode.ADD_MODE.label) {
                        val imagePaths = withContext(Dispatchers.IO) {
                            imageUriStringList.map { it.toUri().cropToJpeg(context, 1, 1) }
                        }
                        imageUrls = imageRepository.addImages(imagePaths)
                    } else { // 수정 모드일 경우
                        imageUrls = withContext(Dispatchers.IO) {
                            imageUriStringList.map {
                                if (URLUtil.isContentUrl(it)) {
                                    val jpeg = it.toUri().cropToJpeg(context, 1, 1)
                                    imageRepository
                                        .addImages(listOf(jpeg))
                                        .first()
                                } else {
                                    it
                                }
                            }
                        }
                    }
                }

                if (itemExpiration.isNotEmpty()) {
                    itemExpiration = itemExpiration.replace(".", "-") + "T00:00:00.000Z"
                }

                if (screenMode == ScreenMode.ADD_MODE.label) {
                    itemRepository.addItem(
                        containerId = itemLockerId,
                        name = itemName,
                        itemType = itemCategorySelection.toString(),
                        quantity = itemCount,
                        imageUrls = imageUrls,
                        tagIds = tagIds,
                        description = itemMemo,
                        purchaseDate = itemPurchase.replace(".", "-"),
                        // UTC 시간대의 오전 0시 (한국시간 기준으로 09시) 추후 UI 변경사항 고려해서 값 대입할 것.
                        useByDate = itemExpiration,
                        pinX = position?.x,
                        pinY = position?.y
                    )
                } else {
                    itemId?.let { id ->
                        itemRepository.editItem(
                            itemId = id,
                            containerId = itemLockerId,
                            name = itemName,
                            itemType = itemCategorySelection.toString(),
                            quantity = itemCount,
                            imageUrls = imageUrls,
                            tagIds = tagIds,
                            description = itemMemo,
                            purchaseDate = itemPurchase.replace(".", "-"),
                            // UTC 시간대의 오전 0시 (한국시간 기준으로 09시) 추후 UI 변경사항 고려해서 값 대입할 것.
                            useByDate = itemExpiration,
                            pinX = position?.x,
                            pinY = position?.y
                        )
                    }
                }
            }.onSuccess {
                postSideEffect(AddItemSideEffect.ShowToast("저장되었습니다."))
                postSideEffect(AddItemSideEffect.AddItemSucceed)
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
        if (screenMode == ScreenMode.EDIT_MODE.label) {
            postSideEffect(AddItemSideEffect.ShowToast("수정모드에서는 위치 변경이 불가합니다."))
            return
        }
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

    fun moveAddTag() {
        withState<AddItemState.Success> { state ->
            val addItemTags = state.dataList.find { it is AddItemTags } as AddItemTags
            postSideEffect(
                AddItemSideEffect.MoveAddTag(
                    selectedTagList = addItemTags.tagList
                )
            )
        }
    }

    fun setSelectedTags(tagList: List<Tag>) {
        withState<AddItemState.Success> { state ->
            val addItemTags = state.dataList.find { it is AddItemTags } as AddItemTags
            val newDataList = state.dataList.toMutableList().apply {
                set(
                    state.dataList.indexOf(addItemTags), addItemTags.copy(
                        tagList = tagList
                    )
                )
            }
            setState(
                state.copy(
                    dataList = newDataList,
                )
            )
        }
    }

}
