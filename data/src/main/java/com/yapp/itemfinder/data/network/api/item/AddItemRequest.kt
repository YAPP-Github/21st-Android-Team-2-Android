package com.yapp.itemfinder.data.network.api.item

import java.time.LocalDate
import java.time.LocalDateTime

data class AddItemRequest(
    val containerId: Long,
    val name: String,
    val itemType: String,
    val quantity: Int,
    val imageUrls: List<String> = listOf(),
    val tagIds: List<Long> = listOf(),
    val description: String?,
    val purchaseDate: LocalDate?,
    val useByDate: LocalDateTime?,
    val pinX: Float?,
    val pinY: Float?
)
