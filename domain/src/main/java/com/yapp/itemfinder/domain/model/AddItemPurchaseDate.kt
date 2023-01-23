package com.yapp.itemfinder.domain.model

data class AddItemPurchaseDate(
    var purchaseDate: String = "",
    override var type: CellType = CellType.ADD_ITEM_PURCHASE_DATE_CELL
) : Data()
