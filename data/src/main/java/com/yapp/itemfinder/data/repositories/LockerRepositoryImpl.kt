package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.lockerlist.LockerListApi
import com.yapp.itemfinder.domain.coroutines.DispatcherProvider
import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.domain.repository.LockerRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val lockerListApi: LockerListApi
) : LockerRepository {
    override suspend fun getLockers(spacdId: Long): List<Locker> {
        return withContext(dispatcherProvider.io) {
            val response = lockerListApi.getLockersBySpaceId(spacdId)
            val lockerList = response.map { it.refineToLocker() }
            lockerList
        }
    }
}
