package com.yapp.itemfinder.domain.model

data class AddItemLocation(
    var spaceId: Long = 0,
    var spaceName: String = "",
    var lockerId: Long = 0,
    var lockerName: String = "",
    override var type: CellType = CellType.ADD_ITEM_LOCATION_CELL
) : Data()
