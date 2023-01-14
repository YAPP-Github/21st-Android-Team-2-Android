package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.SpaceItem

interface HomeSpaceRepository {

    suspend fun getHomeSpaces(): List<SpaceItem>

}
