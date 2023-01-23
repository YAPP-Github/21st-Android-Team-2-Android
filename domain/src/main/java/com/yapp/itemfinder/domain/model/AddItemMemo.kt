package com.yapp.itemfinder.domain.model

data class AddItemMemo(
    var memo: String = "",
    override var type: CellType = CellType.ADD_ITEM_MEMO_CELL
) : Data()
