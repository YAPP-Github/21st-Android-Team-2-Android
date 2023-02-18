package com.yapp.itemfinder.data.network.api.item

import java.time.LocalDate
import java.time.LocalDateTime

data class AddItemRequest(
    val containerId: Long,
    val name: String,
    val itemType: String,
    val quantity: Int,
    val imageUrls: List<String>? = null,
    val tagIds: List<Long>? = null,
    val description: String?,
    val purchaseDate: String?,
    val useByDate: String?,
    val pinX: Float?,
    val pinY: Float?
)
