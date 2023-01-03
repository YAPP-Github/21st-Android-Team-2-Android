package com.yapp.itemfinder.domain.model

data class Locker(
    val name: String,
    override var type: CellType = CellType.LOCKER_CELL
) : Data() {

    var editHandler: DataHandler = {}
    fun editLocker() = editHandler.invoke(this)

    var deleteHandler: DataHandler = {}
    fun deleteLocker() = deleteHandler.invoke(this)

}
