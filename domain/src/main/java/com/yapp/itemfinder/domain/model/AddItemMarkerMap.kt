package com.yapp.itemfinder.domain.model

data class AddItemMarkerMap(
    override val id: Long = 99,
    val lockerEntity: LockerEntity,
    val item: Item? = null,
    override var type: CellType = CellType.ADD_ITEM_MARKER_MAP_CELL
): Data(id, type)
