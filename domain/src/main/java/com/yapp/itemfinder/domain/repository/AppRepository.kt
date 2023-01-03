package com.yapp.itemfinder.domain.repository

interface AppRepository {

    suspend fun fetchHealthCheck(): String

}
