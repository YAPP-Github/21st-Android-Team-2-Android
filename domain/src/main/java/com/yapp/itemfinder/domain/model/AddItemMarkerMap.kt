package com.yapp.itemfinder.domain.model

typealias ItemCategoryHandler = (ItemCategory) -> Unit

data class AddItemMarkerMap(
    override val id: Long = 99,
    val lockerEntity: LockerEntity,
    val item: Item? = null,
    override var type: CellType = CellType.ADD_ITEM_MARKER_MAP_CELL
): Data(id, type) {

    var moveItemPositionDefineHandler: ActionHandler = { }

    var itemCategorySetHandler: ItemCategoryHandler = { }
    fun runMoveItemPositionDefine() = moveItemPositionDefineHandler.invoke()

    fun runItemCategorySet(itemCategory: ItemCategory) = itemCategorySetHandler(itemCategory)

}
