package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.Thing

interface ThingRepository {
    fun getThingsByLockerId(lockerId :Long): List<Thing>

    fun getAllThings(): List<Thing>
}
