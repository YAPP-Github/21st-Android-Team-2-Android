package com.yapp.itemfinder.data.network.api.item

import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.ItemCategory
import com.yapp.itemfinder.domain.model.Tag

data class ItemResponse(
    val id: Long,
    val name: String,
    val quantity: Int,
    val tags: List<String>,
    val itemType: String,
    val useByDate: String,
    val representativeImageUrl: String,
    val pinX: Float,
    val pinY: Float,
    val spaceName: String,
    val containerName: String
) {
    fun toItem(lockerId: Long) = Item(
        id = id,
        lockerId = lockerId,
        name = name,
        expirationDate = useByDate,
        purchaseDate = null,
        memo = null,
        imageUrls = listOf( representativeImageUrl),
        count = quantity,
        itemCategory = when (itemType) {
            "LIVING" -> ItemCategory.LIVING
            "FOOD" -> ItemCategory.FOOD
            else -> ItemCategory.FASHION
        },
        position = Item.Position(pinX, pinY),
        tags = tags.map { Tag(it) }
    )
}
