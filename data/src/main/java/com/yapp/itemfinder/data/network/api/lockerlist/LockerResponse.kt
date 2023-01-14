package com.yapp.itemfinder.data.network.api.lockerlist

import com.yapp.itemfinder.domain.model.Locker

data class LockerResponse(
    val id: Long,
    val icon: String,
    val spaceId: Long,
    val name: String,
    val imageUrl: String?
) {
    fun refineToLocker() = Locker(
        id = id,
        name = name,
        icon = icon,
        spaceId = spaceId,
        imageUrl = imageUrl
    )
}
