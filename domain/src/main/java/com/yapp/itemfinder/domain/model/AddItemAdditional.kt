package com.yapp.itemfinder.domain.model

data class AddItemAdditional(
    var hasMemo: Boolean = false,
    var hasExpirationDate: Boolean = false,
    var hasPurchaseDate: Boolean = false,
    override var type: CellType = CellType.ADD_ITEM_ADDITIONAL_CELL
) : Data() {
    var addMemoHandler: ActionHandler = {}
    fun addMemo() = addMemoHandler.invoke()

    var addExpirationDateHandler: ActionHandler = {}
    fun addExpirationDate() = addExpirationDateHandler.invoke()

    var addPurchaseDateHandler: ActionHandler = {}
    fun addPurchaseDate() = addPurchaseDateHandler.invoke()
}
