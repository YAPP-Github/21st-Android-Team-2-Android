package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.LockerEntity

interface LockerRepository {
    suspend fun getLockers(spaceId: Long): List<LockerEntity>
}
