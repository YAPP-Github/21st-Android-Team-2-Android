package com.yapp.itemfinder.data.network.api.item

import java.time.LocalDate
import java.time.LocalDateTime

data class AddItemResponse(
//    val id: Long, //fixme: 추가예정
    val name: String,
    val itemType: String,
    val quantity: Int,
    val imageUrls: List<String>,
    val tags: List<String>,
    val description: String?,
    val purchaseDate: LocalDate?,
    val useByDate: LocalDateTime?,
    val pinX: Float?,
    val pinY: Float?
)
