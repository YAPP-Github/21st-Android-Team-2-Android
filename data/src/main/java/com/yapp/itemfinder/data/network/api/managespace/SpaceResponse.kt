package com.yapp.itemfinder.data.network.api.managespace

import com.yapp.itemfinder.domain.model.ManageSpaceEntity
import com.yapp.itemfinder.domain.model.SelectSpace

data class SpaceResponse(
    val name: String,
    val id: Int
) {

    fun toManageSpaceEntity() = ManageSpaceEntity(
        name = name,
        id = id.toLong()
    )

    fun toSelectSpaceEntity() = SelectSpace(
        name = name,
        id = id.toLong()
    )
}
