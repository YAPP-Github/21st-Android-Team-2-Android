package com.yapp.itemfinder.domain.repository

interface AppRepository {

    suspend fun getAppData(): List<Any>

}
