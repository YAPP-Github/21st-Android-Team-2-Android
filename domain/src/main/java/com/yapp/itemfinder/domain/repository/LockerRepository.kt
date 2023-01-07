package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.Data

interface LockerRepository {
    suspend fun getAllLockers(): List<Data>
}
