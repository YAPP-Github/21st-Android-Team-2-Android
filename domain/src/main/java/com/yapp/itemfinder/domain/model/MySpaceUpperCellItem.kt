package com.yapp.itemfinder.domain.model

typealias MySpaceUpperCellItemHandler = (MySpaceUpperCellItem) -> Unit

class MySpaceUpperCellItem(
    val title: String,
    override var type: CellType = CellType.HOMETAB_MYSPACE_UPPER_CELL
): Data() {

    var spaceManagementDetailHandler: MySpaceUpperCellItemHandler = {}

    fun runSpaceManagementDetail() = spaceManagementDetailHandler(this)

}
