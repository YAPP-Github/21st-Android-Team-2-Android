package com.yapp.itemfinder.domain.model

data class LikeItem(
    override var id: Long,
    val name: String = "like default",
    override var type: CellType = CellType.LIKE_CELL
): Data(){

    fun goLikeDetailPage() = handler.invoke(this)

    var deleteHandler: DataHandler = {}
    fun deleteLikeItem() = deleteHandler.invoke(this)

    var updateHandler: DataHandler = {}
    fun updateLikeItem() = updateHandler.invoke(this)
}