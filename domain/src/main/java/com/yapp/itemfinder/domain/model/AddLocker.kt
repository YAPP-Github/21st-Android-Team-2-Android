package com.yapp.itemfinder.domain.model

data class AddLocker(
    override var type: CellType = CellType.ADD_LOCKER_CELL
): Data(){
    var addLockerHandler: DataHandler = {}
    fun addLocker() = addLockerHandler.invoke(this)
}
