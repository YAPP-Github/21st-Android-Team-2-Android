package com.yapp.itemfinder.domain.model

data class AddItemImages(
    var uriStringList: MutableList<String>,
    override var type: CellType = CellType.ADD_ITEM_IMAGES_CELL,
    val maxCount: Int = 10
): Data(){
    var addCameraActionHandler: () -> Unit = {}
    fun addCameraAction() = addCameraActionHandler.invoke()

    var cancelImageUploadHandler: () -> Unit = {}
    fun cancelImageUpload() = cancelImageUploadHandler.invoke()
}
