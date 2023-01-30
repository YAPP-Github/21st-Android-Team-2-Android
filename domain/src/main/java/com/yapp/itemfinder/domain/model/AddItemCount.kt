package com.yapp.itemfinder.domain.model

data class AddItemCount(
    var count: Int = 1,
    override var type: CellType = CellType.ADD_ITEM_COUNT_CELL
) : Data() {
    var plusHandler: ActionHandler = {}
    fun plusCount() = plusHandler.invoke()

    var minusHandler: ActionHandler = {}
    fun minusCount() = minusHandler.invoke()
}
