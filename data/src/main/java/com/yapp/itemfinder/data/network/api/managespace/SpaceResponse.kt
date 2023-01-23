package com.yapp.itemfinder.data.network.api.managespace

import com.yapp.itemfinder.domain.model.ManageSpaceEntity

data class SpaceResponse(
    val name: String,
    val id: Int
) {

    fun toManageSpaceEntity() = ManageSpaceEntity(
        name = name,
        id = id.toLong()
    )

}
