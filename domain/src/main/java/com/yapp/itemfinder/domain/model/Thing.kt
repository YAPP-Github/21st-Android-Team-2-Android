package com.yapp.itemfinder.domain.model

data class Thing(
    override var id: Long,
    var lockerId: Long,
    var name: String,
    var expirationDate: String?,
    var imageUrl: String?,
    val thingCategory: ThingCategory?,
    var tags: List<Tag>?,
    override var type:  CellType = CellType.THING_IN_LIST_CELL

) : Data() {

}

enum class ThingCategory {
    LIFE,
    FOOD,
    FASHION
}
