package com.yapp.itemfinder.domain.model

data class AddItemInfoRequired(
    val name: String,
    val category: String = ItemCategorySelection.DEFAULT.label,
    val location: String,
    val count: Int = 1,
    override var type: CellType = CellType.ADD_ITEM_INFO_REQUIRED_CELL
) : Data() {
    var selectCategoryHandler: ActionHandler = {}
    fun selectCategory() = selectCategoryHandler.invoke()
}

enum class ItemCategorySelection(val label: String) {
    DEFAULT("카테고리"),
    LIFE("생활"),
    FOOD("식품"),
    FASHION("패션")
}
