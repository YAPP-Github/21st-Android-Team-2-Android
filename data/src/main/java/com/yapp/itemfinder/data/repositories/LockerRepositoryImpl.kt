package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.locker.AddEditLockerRequest
import com.yapp.itemfinder.data.network.api.locker.LockerApi
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.repository.LockerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerRepositoryImpl @Inject constructor(
    private val lockerApi: LockerApi
) : LockerRepository {

    override suspend fun getLockerById(lockerId: Long): LockerEntity? =
        lockerApi.getLockerById(lockerId).refineToLocker()

    override suspend fun getLockers(spaceId: Long): List<LockerEntity> =
        lockerApi.getLockersBySpaceId(spaceId).map { it.refineToLocker() }

    override suspend fun addLocker(
        name: String,
        url: String?,
        spaceId: Long,
        icon: String
    ): LockerEntity {
        return lockerApi.addNewLocker(
            AddEditLockerRequest(
                name, url, spaceId, icon
            )
        ).refineToLocker()
    }

    override suspend fun editLocker(
        name: String,
        url: String?,
        spaceId: Long,
        icon: String,
        lockerId: Long
    ): LockerEntity {
        return lockerApi.editLocker(
            AddEditLockerRequest(
                name, url, spaceId, icon
            ),
            lockerId
        ).refineToLocker()
    }
}
