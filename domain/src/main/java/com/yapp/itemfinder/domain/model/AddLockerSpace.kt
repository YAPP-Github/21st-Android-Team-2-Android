package com.yapp.itemfinder.domain.model

data class AddLockerSpace(
    override var type: CellType = CellType.ADD_LOCKER_SPACE_CELL,
    var name: String
) : Data() {
    var selectSpaceHandler: ActionHandler = {}
    fun selectSpace() = selectSpaceHandler.invoke()
}
