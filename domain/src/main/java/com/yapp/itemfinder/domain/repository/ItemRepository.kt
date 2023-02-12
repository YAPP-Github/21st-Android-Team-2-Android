package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.Item
import java.time.LocalDate
import java.time.LocalDateTime

interface ItemRepository {
    suspend fun getItemsByLockerId(lockerId: Long): List<Item>

    suspend fun getItemById(itemId: Long): Item

    suspend fun getAllItems(): List<Item>

    suspend fun addItem(
        containerId: Long,
        name: String,
        itemType: String,
        quantity: Int,
        imageUrls: List<String>? = null,
        tagIds: List<Long> ? = null,
        description: String?,
        purchaseDate: LocalDate?,
        useByDate: LocalDateTime?,
        pinX: Float?,
        pinY: Float?
        ): Item
}
