package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Locker

interface LockerRepository {
    suspend fun getAllLockers(): List<Data>
    suspend fun getLocker(id: Long): Locker
}
