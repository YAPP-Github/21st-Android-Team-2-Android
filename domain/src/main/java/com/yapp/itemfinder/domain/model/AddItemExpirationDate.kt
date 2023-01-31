package com.yapp.itemfinder.domain.model

data class AddItemExpirationDate(
    var expirationDate: String = "",
    override var type: CellType = CellType.ADD_ITEM_EXPIRATION_DATE_CELL
) : Data() {
    var openDatePickerHandler: ActionHandler = {}
    fun openDatePicker() = openDatePickerHandler.invoke()
}
