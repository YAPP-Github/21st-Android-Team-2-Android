package com.yapp.itemfinder.data.network.api.home

import com.google.gson.annotations.SerializedName
import com.yapp.itemfinder.domain.model.SpaceItem

data class HomeSpaceResponse(
    val spaceId: Long,
    val spaceName: String,
    val containerCount: Int,
    val imageUrl: String?,
    @SerializedName("topContainers")
    val topContainers: List<HomeSpaceTopLockerResponse>
) {
    fun refineToSpaceItem() = SpaceItem(
        id = spaceId,
        name = spaceName,
        lockerList = topContainers.map { it.refineToLocker() },
        lockerCount = containerCount
    )
}

