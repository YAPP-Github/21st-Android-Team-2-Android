package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.ItemCategory
import com.yapp.itemfinder.domain.repository.ItemRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemMockRepositoryImpl @Inject constructor() : ItemRepository {

    private val sample = Item(
        id = (0..100).random().toLong(),
        lockerId = 1,
        itemCategory = ItemCategory.FOOD,
        name = "선크림",
        expirationDate = "2022.12.25까지",
        imageUrl = "http://source.unsplash.com/random/150x150",
        tags = listOf(Tag("생활"), Tag("화장품")),
        count = 1
    )
    private val sampleLongTag = sample.copy(
        tags = mutableListOf<Tag>().apply {
            for (i in 1..5) {
                addAll(listOf(Tag("생활"), Tag("화장품")))
            }
        },
        count = 3
    )

    override fun getAllItems(): List<Item> {
        return mutableListOf<Item>().apply {
            add(
                sampleLongTag.copy(
                    id = (0..100).random().toLong(),
                    position = Item.Position(
                        (0..100).random().toFloat(),
                        (0..100).random().toFloat(),
                    )
                )
            )
            for (i in 1..10)
                add(
                    sample.copy(
                        id = (0..100).random().toLong(),
                        position = Item.Position(
                            (0..100).random().toFloat(),
                            (0..100).random().toFloat(),
                        )
                    )
                )
        }
    }

    override fun getItemsByLockerId(lockerId: Long): List<Item> {
        return mutableListOf<Item>().apply {
            add(
                sampleLongTag.copy(
                    id = (0..100).random().toLong(),
                    position = Item.Position(
                        (0..100).random().toFloat(),
                        (0..100).random().toFloat(),
                    )
                )
            )
            for (i in 1..10)
                add(
                    sample.copy(
                        id = (0..100).random().toLong(),
                        position = Item.Position(
                            (0..100).random().toFloat(),
                            (0..100).random().toFloat(),
                        )
                    )
                )
        }
    }

}