package com.yapp.itemfinder.data.network.api.item

import com.yapp.itemfinder.domain.model.AddItemPurchaseDate
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.ItemCategory
import com.yapp.itemfinder.domain.model.Tag

/**
 * 원래 네이밍을 ItemResponse로 하려고 했으나, ItemResponseElement로 작성될 클래스가 ItemResponse로 되어있었고
 * 이를 ItemResponseElement라고 변경하였습니다.따라서 현재 클래스를 ItemResponse로 네이밍할 경우 혼동이 발생할 수 있어 이렇게 네이밍합니다.
 * 차후에 ItemResponse로 바꾸면 좋겠습니다.
 */
data class ItemDetailResponse(
    val id: Long,
    val name: String,
    val quantity: Int,
    val tags: List<String>,
    val itemType: String,
    val imageUrls: List<String>,
    val containerImageUrl: String,
    val description: String,
    val pinX: Float,
    val pinY: Float,
    val spaceName: String,
    val containerName: String,
    val purchaseDate: String,
    val useByDate: String,
) {
    fun toItem(lockerId: Long? = null) = Item(
        id = id,
        lockerId = lockerId,
        name = name,
        expirationDate = useByDate,
        purchaseDate = purchaseDate,
        memo = description,
        imageUrls = imageUrls,
        containerImageUrl =containerImageUrl,
        count = quantity,
        itemCategory = when (itemType) {
            "LIFE" -> ItemCategory.LIFE
            "FOOD" -> ItemCategory.FOOD
            else -> ItemCategory.FASHION
        },
        position = Item.Position(pinX, pinY),
        tags = tags.map { Tag(it) }
    )
}
