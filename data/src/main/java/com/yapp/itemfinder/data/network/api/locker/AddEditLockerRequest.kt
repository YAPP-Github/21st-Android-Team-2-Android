package com.yapp.itemfinder.data.network.api.locker

data class AddEditLockerRequest(
    val name: String,
    val url: String? = null,
    val spaceId: Long,
    val icon: String
)
