package com.yapp.itemfinder.domain.model

class CellItem(
    override var type: CellType = CellType.HOMETAB_MYSPACE_UPPER_CELL
): Data() {
    var action: ActionHandler = {}
}
