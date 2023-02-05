package com.yapp.itemfinder.domain.model

data class AddItemMarkerMap(
    override var id: Long = 99,
    val item: Item? = null,
    override var type: CellType = CellType.ADD_ITEM_MARKER_MAP_CELL
): Data(id, type)
