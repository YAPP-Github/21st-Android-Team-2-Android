package com.yapp.itemfinder.domain.model

data class AddItemImages(
    var uriStringList: List<String>,
    override var type: CellType = CellType.ADD_ITEM_IMAGES_CELL
): Data()
