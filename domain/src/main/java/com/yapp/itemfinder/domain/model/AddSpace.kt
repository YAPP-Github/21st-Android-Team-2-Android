package com.yapp.itemfinder.domain.model

data class AddSpace(
    override var type: CellType = CellType.ADD_SPACE_CELL
) : Data() {
    var addSpaceHandler: ActionHandler = {}
    fun addSpace() = addSpaceHandler.invoke()
}
