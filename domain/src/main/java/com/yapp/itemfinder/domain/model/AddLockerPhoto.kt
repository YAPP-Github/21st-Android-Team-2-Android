package com.yapp.itemfinder.domain.model

data class AddLockerPhoto(
    override var type: CellType = CellType.ADD_LOCKER_IMAGE_CELL,
    var url: String? = null,
    var mode: ScreenMode
) : Data() {
    var uploadImageHandler: () -> Unit = {}
    fun uploadImage() = uploadImageHandler.invoke()
}
