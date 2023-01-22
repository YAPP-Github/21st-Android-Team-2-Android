package com.yapp.itemfinder.domain.model

data class AddItemName(
    val name: String = "",
    override var type: CellType = CellType.ADD_ITEM_NAME_CELL
) : Data()