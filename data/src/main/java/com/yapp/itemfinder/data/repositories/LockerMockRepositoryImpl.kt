package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.AppApi
import com.yapp.itemfinder.domain.model.AddLocker
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.domain.repository.LockerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockerMockRepositoryImpl @Inject constructor(
    private val appApi: AppApi
): LockerRepository{
    override suspend fun getAllLockers(): List<Data> {
        // api call
        return listOf(
            AddLocker(),
            Locker(name = "화장대"),
            Locker(name = "냉장고"),
            Locker(name = "옷장")
        )
    }

}
