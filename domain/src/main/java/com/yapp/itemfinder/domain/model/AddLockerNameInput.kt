package com.yapp.itemfinder.domain.model

data class AddLockerNameInput(
    val name: String? = null,
    override var type: CellType = CellType.ADD_LOCKER_NAME_INPUT_CELL,
    var mode: ScreenMode
) : Data() {
    var enterHandler: StringHandler = {}
    fun enterName(name: String) = enterHandler.invoke(name)

    var saveHandler: ActionHandler = { }
    fun saveName() = saveHandler.invoke()

}
