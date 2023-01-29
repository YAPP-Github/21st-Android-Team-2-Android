package com.yapp.itemfinder.domain.model

data class Item(
    override var id: Long,
    var lockerId: Long,
    var name: String,
    var expirationDate: String?,
    var purchaseDate: String?,
    var memo: String?,
    var imageUrl: String?,
    val itemCategory: ItemCategory?,
    var tags: List<Tag>?,
    val count: Int = 0,
    override var type: CellType = CellType.ITEM_SIMPLE_CELL

) : Data() {

}

enum class ItemCategory(val label: String) {
    LIFE("생활"),
    FOOD("식품"),
    FASHION("패션")
}
