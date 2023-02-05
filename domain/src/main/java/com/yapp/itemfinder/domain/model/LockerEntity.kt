package com.yapp.itemfinder.domain.model

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LockerEntity(
    override val id: Long,
    override val type: CellType = CellType.LOCKER_CELL,
    val name: String,
    val icon: String,
    val spaceId: Long,
    val imageUrl: String? = null
) : Data() {

    @IgnoredOnParcel
    var moveLockerDetailHandler: DataHandler = {}
    fun moveLockerDetail() = moveLockerDetailHandler.invoke(this)

    @IgnoredOnParcel
    var editHandler: DataHandler = {}
    fun editLocker() = editHandler.invoke(this)

    @IgnoredOnParcel
    var deleteHandler: DataHandler = {}
    fun deleteLocker() = deleteHandler.invoke(this)

}
