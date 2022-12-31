package com.yapp.itemfinder.splash

import com.yapp.itemfinder.data.network.api.AppApi
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SplashScreenRepository @Inject constructor(
    private val api: AppApi
) {
    suspend fun getAppData(): List<Any> {
        delay(2000L)
        return listOf()
    }
}
