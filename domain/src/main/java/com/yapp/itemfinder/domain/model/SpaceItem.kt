package com.yapp.itemfinder.domain.model

data class SpaceItem(
    override var id: Long,
    val name: String,
    val lockerList: List<LockerEntity>,
    val lockerCount: Int,
    override var type: CellType = CellType.SPACE_CELL
) : Data(){
    var detailHandler: DataHandler =  {}
    fun moveSpaceDetailPage() = detailHandler.invoke(this)
    var moveLockerDetailHandler: (LockerEntity) -> Unit = {}
}
