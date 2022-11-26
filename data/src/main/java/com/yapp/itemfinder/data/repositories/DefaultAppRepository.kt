package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.AppApi
import com.yapp.itemfinder.domain.repository.AppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultAppRepository @Inject constructor(
    private val api: AppApi
): AppRepository {

    override suspend fun getAppData(): List<Any> {
        TODO("Not yet implemented")
    }

}
