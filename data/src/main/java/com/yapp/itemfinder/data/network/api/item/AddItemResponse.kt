package com.yapp.itemfinder.data.network.api.item

import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.Tag

data class AddItemResponse(
    val id: Long,
    val name: String,
    val itemType: String,
    val quantity: Int,
    val imageUrls: List<String>,
    val tags: List<String>,
    val description: String?,
    val purchaseDate: String?,
    val useByDate: String?,
    val pinX: Float?,
    val pinY: Float?
){
    fun toItem(): Item {
        return Item(
            id = id,
            name = name,
            count = quantity,
            imageUrls = imageUrls,
            tags= tags.map { Tag(it.hashCode().toLong(), it) },
            memo = description,
            position = if (pinX != null && pinY != null) Item.Position(pinX, pinY) else null,
            purchaseDate = purchaseDate
        )
    }

}
