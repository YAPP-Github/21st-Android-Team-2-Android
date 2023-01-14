package com.yapp.itemfinder.data.network.api.home

import com.yapp.itemfinder.domain.model.Locker

data class HomeSpaceTopLockerResponse(
    val id: Long,
    val icon: String,
    val spaceId: Long,
    val name: String,
    val description: String?,
    val imageUrl: String?
) {
    fun refineToLocker(): Locker {
        return Locker(
            id = id,
            icon = icon,
            name = name,
            spaceId = spaceId
        )
    }
}
