package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.item.*
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.ItemCategory
import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.data.network.api.item.ItemApi
import com.yapp.itemfinder.domain.repository.ItemRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepositoryImpl @Inject constructor(
    private val itemApi: ItemApi
) : ItemRepository {
    override suspend fun getItemsByLockerId(lockerId: Long): List<Item> {
        val response = itemApi.searchItems(
            ItemSearchRequest(
                searchTarget = ItemSearchTarget(
                    location = SearchLocation.CONTAINER.label,
                    id = lockerId
                )
            )
        )
        return response.data.map { it.toItem(lockerId) }
    }
    //fixme : 서버에 연결
    override suspend fun getItemById(itemId: Long): Item {
        return Item(
            id = (0..100).random().toLong(),
            lockerId = 1,
            itemCategory = ItemCategory.FOOD,
            name = "선크림",
            expirationDate = "2022.12.25.",
            purchaseDate = null,
            memo = null,
            imageUrls = listOf("http://source.unsplash.com/random/150x150"),
            tags = listOf(Tag("생활"), Tag("화장품")),
            count = 1
        )
    }

    override suspend fun getAllItems(): List<Item> {
        return listOf()
    }

    override suspend fun addItem(
        containerId: Long,
        name: String,
        itemType: String,
        quantity: Int,
        imageUrls: List<String>?,
        tagIds: List<Long>?,
        description: String?,
        purchaseDate: LocalDate?,
        useByDate: LocalDateTime?,
        pinX: Float?,
        pinY: Float?
    ): Item {
        return  itemApi.addNewItem(
            AddItemRequest(containerId = containerId,
                name = name,
                quantity = quantity,
                itemType = itemType,
                imageUrls = imageUrls,
                tagIds = tagIds,
                description = description,
                purchaseDate = purchaseDate,
                useByDate = useByDate,
                pinX = pinX,
                pinY = pinY,
            )
        ).toItem()
    }

}
