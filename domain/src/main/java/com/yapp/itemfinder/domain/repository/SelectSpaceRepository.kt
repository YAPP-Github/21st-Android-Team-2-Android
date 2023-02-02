package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.SelectSpace

interface SelectSpaceRepository {
    suspend fun getAllSpaces(): List<SelectSpace>
}
