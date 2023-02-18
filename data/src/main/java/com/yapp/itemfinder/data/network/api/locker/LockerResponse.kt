package com.yapp.itemfinder.data.network.api.locker

import com.yapp.itemfinder.domain.model.LockerEntity

data class LockerResponse(
    val id: Long,
    val icon: String,
    val spaceId: Long,
    val name: String,
    val imageUrl: String?
) {
    fun refineToLocker() = LockerEntity(
        id = id,
        name = name,
        icon = icon,
        spaceId = spaceId,
        imageUrl = imageUrl
    )
}
