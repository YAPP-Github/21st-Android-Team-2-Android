package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.Item

interface ItemRepository {
    suspend fun getItemsByLockerId(lockerId: Long): List<Item>

    fun getItemById(itemId: Long): Item

    fun getAllItems(): List<Item>

    fun addItem(): Item
}
