package com.yapp.itemfinder.domain.model

data class AddLockerPhoto(
    override var type: CellType = CellType.ADD_LOCKER_IMAGE_CELL,
    var uriString: String? = null
) : Data() {
    var uploadImageHandler: () -> Unit = {}
    fun uploadImage() = uploadImageHandler.invoke()
}
