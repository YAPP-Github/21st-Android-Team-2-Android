package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.domain.model.Thing
import com.yapp.itemfinder.domain.repository.ThingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThingMockRepositoryImpl @Inject constructor() : ThingRepository {

    private val sample = Thing(
        id = 1,
        lockerId = 1,
        name = "선크림",
        expirationDate = "2022.12.25까지",
        imageUrl = "http://source.unsplash.com/random/150x150",
        tags = listOf(Tag("생활"), Tag("화장품"))
    )

    override fun getAllThings(): List<Thing> {
        return mutableListOf<Thing>().apply {
            for (i in 1..10)
                add(sample)
        }
    }

    override fun getThingsByLockerId(lockerId: Long): List<Thing> {
        throw NotImplementedError("NOT IMPLEMENTED")
    }
}
