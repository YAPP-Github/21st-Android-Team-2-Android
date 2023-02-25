package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.AppApi
import com.yapp.itemfinder.domain.data.SecureLocalDataStore
import com.yapp.itemfinder.domain.repository.AppRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultAppRepository @Inject constructor(
    private val appApi: AppApi,
    private val secureLocalDataStore: SecureLocalDataStore,
): AppRepository {

    override suspend fun fetchHealthCheck(): String {
        return appApi.fetchHealthCheck().body().toString()
    }

}
