package com.yapp.itemfinder.domain.model

data class AddItemCategory(
    var category: ItemCategorySelection = ItemCategorySelection.DEFAULT,
    override var type: CellType = CellType.ADD_ITEM_CATEGORY_CELL
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
