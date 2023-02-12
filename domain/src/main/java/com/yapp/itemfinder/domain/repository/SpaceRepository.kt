package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.SelectSpace

interface SpaceRepository {
    suspend fun getAllSpaces(): List<SelectSpace>

}
