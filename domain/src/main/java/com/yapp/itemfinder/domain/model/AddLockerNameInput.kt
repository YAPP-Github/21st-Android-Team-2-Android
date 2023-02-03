package com.yapp.itemfinder.domain.model

data class AddLockerNameInput(
    override var type: CellType = CellType.ADD_LOCKER_NAME_INPUT_CELL
) : Data() {
    var enterHandler: EnterStringHandler = {}
    fun enterName(name: String) = enterHandler.invoke(name)
}
