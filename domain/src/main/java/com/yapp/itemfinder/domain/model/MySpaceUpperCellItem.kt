package com.yapp.itemfinder.domain.model

class MySpaceUpperCellItem(
    val title: String,
    override var type: CellType = CellType.HOMETAB_MYSPACE_UPPER_CELL
): Data() {
    var runSpaceManagementPage: ActionHandler = {}
}
