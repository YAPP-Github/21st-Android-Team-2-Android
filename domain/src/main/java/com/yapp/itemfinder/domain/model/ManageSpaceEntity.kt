package com.yapp.itemfinder.domain.model

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ManageSpaceEntity(
    val name: String,
    override var id: Long = 0,
    override var type: CellType = CellType.MANAGE_SPACE_CELL
) : Data() {

    @IgnoredOnParcel
    var editSpaceDialogHandler: DataHandler = {}
    fun editSpaceDialog() = editSpaceDialogHandler.invoke(this)

    @IgnoredOnParcel
    var deleteSpaceDialogHandler: DataHandler = {}
    fun deleteSpaceDialog() = deleteSpaceDialogHandler.invoke(this)

    fun mapAddItemSelectSpaceEntity() =
        AddItemSelectSpaceEntity(id = id, name = name)

}
