package com.yapp.itemfinder.domain.model

data class Category(
    var name: String = "default_name",
    override var type: CellType = CellType.CATEGORY_CELL
) : Data() {

    fun goCategoryPage() = handler.invoke(this)
}
