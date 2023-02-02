package com.yapp.itemfinder.domain.model

typealias AddItemSelectSpaceHandler = (AddItemSelectSpaceEntity) -> Unit

data class AddItemSelectSpaceEntity(
    val name: String,
    override var id: Long = 0,
    override var type: CellType = CellType.ADD_ITEM_SELECT_SPACE_CELL
) : Data(){

    var selectSpaceHandler: AddItemSelectSpaceHandler = {}
    fun runSelectSpace() = selectSpaceHandler.invoke(this)

}
