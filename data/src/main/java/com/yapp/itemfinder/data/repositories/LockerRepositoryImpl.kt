package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.domain.model.AddLockerRequest
import com.yapp.itemfinder.data.network.api.lockerlist.LockerApi
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.repository.LockerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerRepositoryImpl @Inject constructor(
    private val lockerApi: LockerApi
) : LockerRepository {

    override suspend fun getLockers(spaceId: Long): List<LockerEntity> =
        lockerApi.getLockersBySpaceId(spaceId).map { it.refineToLocker() }

    override suspend fun addLocker(locker: AddLockerRequest): LockerEntity =
        lockerApi.addNewLocker(locker).refineToLocker()
    
}
