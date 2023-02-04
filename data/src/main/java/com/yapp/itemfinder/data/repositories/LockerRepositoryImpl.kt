package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.lockerlist.LockerListApi
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.repository.LockerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerRepositoryImpl @Inject constructor(
    private val lockerListApi: LockerListApi
) : LockerRepository {
    override suspend fun getLockers(spaceId: Long): List<LockerEntity> =
        lockerListApi.getLockersBySpaceId(spaceId).map { it.refineToLocker() }

}
