package com.yapp.itemfinder.domain.model

data class SelectSpace(
    val name: String,
    override var id: Long = 0,
    override var type: CellType = CellType.SELECT_SPACE_CELL,
    var isChecked: Boolean = false
) : Data() {
    var checkHandler: ActionHandler = {}
    fun checkSelectSpace() = checkHandler.invoke()
}
