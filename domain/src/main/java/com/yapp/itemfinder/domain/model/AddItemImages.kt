package com.yapp.itemfinder.domain.model

data class AddItemImages(
    var uriStringList: List<String>,
    override var type: CellType = CellType.ADD_ITEM_IMAGES_CELL,
    val maxCount: Int = 10
): Data(){
    var addCameraActionHandler: () -> Unit = {}
    fun addCameraAction() = addCameraActionHandler.invoke()

    var cancelImageUploadHandler: (List<String>) -> Unit = {}
    fun cancelImageUpload(urlStringList: List<String>) = cancelImageUploadHandler.invoke(urlStringList)
}
