package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.Locker

interface LockerRepository {
    suspend fun getLockers(spaceId: Long): List<Locker>
}
