package com.yapp.itemfinder.domain.model

data class AddItemTags(
    var tagList: List<String>, // List<Tag>
    override var type: CellType = CellType.ADD_ITEM_TAGS_CELL
): Data()
