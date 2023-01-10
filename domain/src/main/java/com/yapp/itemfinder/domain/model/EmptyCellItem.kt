package com.yapp.itemfinder.domain.model

class EmptyCellItem(
    val heightDp: Int,
    override var type: CellType = CellType.EMPTY_CELL
): Data()
