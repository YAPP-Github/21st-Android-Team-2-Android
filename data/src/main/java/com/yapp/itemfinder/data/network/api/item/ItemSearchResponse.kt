package com.yapp.itemfinder.data.network.api.item

data class ItemSearchResponse(
    val totalCount: Int,
    val totalPages: Int,
    val currentPageNumber: Int,
    val hasNext: Boolean,
    val data: List<ItemResponse>
)
