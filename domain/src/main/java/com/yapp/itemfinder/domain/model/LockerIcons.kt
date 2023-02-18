package com.yapp.itemfinder.domain.model

data class LockerIcons(
    var icon: String = LockerIconId.LOCKER_ICON1.iconId,
    override var type: CellType = CellType.LOCKER_ICONS_CELL,
    var mode: ScreenMode
) : Data() {
    var changeIconHandler: IconHandler = {}
    fun changeIcon(icon: String) = changeIconHandler.invoke(icon)
}

enum class LockerIconId(val iconId: String) {
    LOCKER_ICON1("IC_CONTAINER_1"),
    LOCKER_ICON2("IC_CONTAINER_2"),
    LOCKER_ICON3("IC_CONTAINER_3"),
    LOCKER_ICON4("IC_CONTAINER_4"),
    LOCKER_ICON5("IC_CONTAINER_5"),
    LOCKER_ICON6("IC_CONTAINER_6"),
    LOCKER_ICON7("IC_CONTAINER_7"),
    LOCKER_ICON8("IC_CONTAINER_8")
}
