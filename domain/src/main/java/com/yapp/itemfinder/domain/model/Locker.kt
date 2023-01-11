package com.yapp.itemfinder.domain.model

data class Locker(
    override var id: Long,
    val name: String,
    val icon: Int,
    override var type: CellType = CellType.LOCKER_CELL
) : Data() {

    var moveLockerDetailHandler: DataHandler = {}
    fun moveLockerDetail() = moveLockerDetailHandler.invoke(this)

    var editHandler: DataHandler = {}
    fun editLocker() = editHandler.invoke(this)

    var deleteHandler: DataHandler = {}
    fun deleteLocker() = deleteHandler.invoke(this)

}
