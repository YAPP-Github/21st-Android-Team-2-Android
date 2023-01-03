package com.yapp.itemfinder.domain.model

data class Locker(
    val name: String,
    override var type: CellType = CellType.LOCKER_CELL
) : Data()