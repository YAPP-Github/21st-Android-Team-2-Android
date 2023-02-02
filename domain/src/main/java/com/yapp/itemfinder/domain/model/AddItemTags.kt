package com.yapp.itemfinder.domain.model

data class AddItemTags(
    var tagList: List<Tag>,
    override var type: CellType = CellType.ADD_ITEM_TAGS_CELL
): Data()
