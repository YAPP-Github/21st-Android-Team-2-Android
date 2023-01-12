package com.yapp.itemfinder.domain.model

data class ManageSpaceItem(
    val name: String,
    override var id: Long = 0,
    override var type: CellType = CellType.MANAGE_SPACE_CELL
) : Data(){

    var editSpaceDialogHandler: DataHandler = {}
    fun editSpaceDialog() = editSpaceDialogHandler.invoke(this)

    var deleteSpaceDialogHandler: DataHandler = {}
    fun deleteSpaceDialog() = deleteSpaceDialogHandler.invoke(this)
}
