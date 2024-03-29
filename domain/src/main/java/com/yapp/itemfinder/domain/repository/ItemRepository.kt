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
        tagIds: List<Long>? = null,
        description: String? = null,
        purchaseDate: String? = null,
        useByDate: String? = null,
        pinX: Float? = null,
        pinY: Float? = null
    ): Item

    suspend fun editItem(
        itemId: Long,
        containerId: Long,
        name: String,
        itemType: String,
        quantity: Int,
        imageUrls: List<String>? = null,
        tagIds: List<Long>? = null,
        description: String? = null,
        purchaseDate: String? = null,
        useByDate: String? = null,
        pinX: Float? = null,
        pinY: Float? = null
    ): Item

    suspend fun deleteItem(itemId: Long)

}
