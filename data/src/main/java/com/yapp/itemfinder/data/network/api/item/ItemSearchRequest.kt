package com.yapp.itemfinder.data.network.api.item

data class ItemSearchRequest(
    val sortOrderOption: String? = "RecentCreated",
    val itemTypes: List<String> = listOf("LIFE", "FASHION", "FOOD"),
    val tagNames: List<String>? = null,
    val itemName: String? = null,
    val searchTarget: ItemSearchTarget?
)
