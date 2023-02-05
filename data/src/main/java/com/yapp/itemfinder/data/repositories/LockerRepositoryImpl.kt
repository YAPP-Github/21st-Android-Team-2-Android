package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.domain.model.AddLockerRequest
import com.yapp.itemfinder.data.network.api.lockerlist.LockerApi
import com.yapp.itemfinder.domain.coroutines.DispatcherProvider
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.repository.LockerRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val lockerApi: LockerApi
) : LockerRepository {
    override suspend fun getLockers(spacdId: Long): List<LockerEntity> {
        return withContext(dispatcherProvider.io) {
            val response = lockerApi.getLockersBySpaceId(spacdId)
            val lockerList = response.map { it.refineToLocker() }
            lockerList
        }
    }

    override suspend fun addLocker(locker: AddLockerRequest): LockerEntity {
        return withContext(dispatcherProvider.io) {
            val response = lockerApi.addNewLocker(locker)
            response.refineToLocker()
        }
    }
}
