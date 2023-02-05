package com.yapp.itemfinder.domain.model

data class AddLockerRequest(
    val name: String,
    val url: String? = null,
    val spaceId: Long,
    val icon: String
)
