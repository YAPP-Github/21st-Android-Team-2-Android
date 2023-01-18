package com.yapp.itemfinder.domain.model

data class LockerEntity(
    override var id: Long,
    val name: String,
    val icon: String,
    val spaceId: Long,
    val imageUrl: String? = null,
    override var type: CellType = CellType.LOCKER_CELL
) : Data() {

    var moveLockerDetailHandler: DataHandler = {}
    fun moveLockerDetail() = moveLockerDetailHandler.invoke(this)

    var editHandler: DataHandler = {}
    fun editLocker() = editHandler.invoke(this)

    var deleteHandler: DataHandler = {}
    fun deleteLocker() = deleteHandler.invoke(this)

}
