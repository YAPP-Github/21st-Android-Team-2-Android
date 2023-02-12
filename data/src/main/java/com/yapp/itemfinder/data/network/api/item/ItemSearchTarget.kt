package com.yapp.itemfinder.data.network.api.item

data class ItemSearchTarget(
    val location: String,
    val id: Long
)

enum class SearchLocation(val label: String) {
    SPACE("SPACE"), CONTAINER("CONTAINER")
}
