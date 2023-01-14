package com.yapp.itemfinder.domain.model

data class SpaceItem(
    override var id: Long,
    val name: String,
    val lockerList: List<Locker>,
    override var type: CellType = CellType.SPACE_CELL
) : Data(){
    var detailHandler: DataHandler =  {}
    fun goSpaceDetailPage() = detailHandler.invoke(this)
    lateinit var lockerDetailHandler: (Locker) -> Unit
}
