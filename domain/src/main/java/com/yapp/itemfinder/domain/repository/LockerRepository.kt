package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.LockerEntity

interface LockerRepository {

    suspend fun getLockerById(lockerId: Long): LockerEntity?
    suspend fun getLockers(spaceId: Long): List<LockerEntity>
    suspend fun addLocker(
        name: String,
        url: String?,
        spaceId: Long,
        icon: String
    ): LockerEntity

    suspend fun editLocker(
        name: String,
        url: String?,
        spaceId: Long,
        icon: String,
        lockerId: Long
    ): LockerEntity
}
