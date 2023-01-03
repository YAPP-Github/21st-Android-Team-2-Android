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
            Locker(id = 1L, name = "화장대"),
            Locker(id = 2L, name = "냉장고"),
            Locker(id = 3L, name = "옷장")
        )
    }

}
