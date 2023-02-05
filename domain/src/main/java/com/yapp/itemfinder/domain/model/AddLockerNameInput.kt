package com.yapp.itemfinder.domain.model

data class AddLockerNameInput(
    override var type: CellType = CellType.ADD_LOCKER_NAME_INPUT_CELL
) : Data() {
    var enterHandler: StringHandler = {}
    fun enterName(name: String) = enterHandler.invoke(name)

    var saveHandler: ActionHandler = { }
    fun saveName() = saveHandler.invoke()

}
