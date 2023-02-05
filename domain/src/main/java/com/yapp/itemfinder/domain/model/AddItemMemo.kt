package com.yapp.itemfinder.domain.model

data class AddItemMemo(
    var memo: String = "",
    override var type: CellType = CellType.ADD_ITEM_MEMO_CELL,
    var mode: ScreenMode
) : Data() {
    var enterHandler: StringHandler = {}
    fun setItemMemo(newMemo: String) = enterHandler.invoke(newMemo)
}
