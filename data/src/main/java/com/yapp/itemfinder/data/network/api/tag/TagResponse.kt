package com.yapp.itemfinder.data.network.api.tag

import com.yapp.itemfinder.domain.model.Tag

data class TagResponse(
    val id: Long,
    val name: String,
) {

    fun toEntity() = Tag(id, name)

}
