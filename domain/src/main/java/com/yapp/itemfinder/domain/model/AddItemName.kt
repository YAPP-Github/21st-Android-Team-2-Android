package com.yapp.itemfinder.domain.model

data class AddItemName(
    val name: String = "",
    override var type: CellType = CellType.ADD_ITEM_NAME_CELL,
    var mode: ScreenMode
) : Data() {
    var enterHandler: StringHandler = {}
    fun setItemName(newName: String) = enterHandler.invoke(newName)

    var saveHandler: ActionHandler = { }
    fun saveName() = saveHandler.invoke()
}

enum class ScreenMode {
    EDIT_MODE,
    ADD_MODE
}
