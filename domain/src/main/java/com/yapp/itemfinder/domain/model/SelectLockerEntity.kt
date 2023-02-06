package com.yapp.itemfinder.domain.model

data class SelectLockerEntity(
    override val id: Long,
    override val type: CellType = CellType.SELECT_LOCKER_CELL,
    val name: String,
    val icon: String,
    val spaceId: Long,
    val imageUrl: String? = null,
    val isSelected: Boolean
) : Data(id = id, type = type) {

    var selectLockerHandler: DataHandler = {}
    fun selectLocker() = selectLockerHandler.invoke(this)

    fun toLockerEntity() = LockerEntity(id, type, name, icon, spaceId)

    companion object {

        fun fromLockerEntity(lockerEntity: LockerEntity, isSelected: Boolean) = SelectLockerEntity(
            id = lockerEntity.id,
            name = lockerEntity.name,
            icon = lockerEntity.icon,
            spaceId = lockerEntity.spaceId,
            imageUrl = lockerEntity.imageUrl,
            isSelected = isSelected,
        )

    }

}
