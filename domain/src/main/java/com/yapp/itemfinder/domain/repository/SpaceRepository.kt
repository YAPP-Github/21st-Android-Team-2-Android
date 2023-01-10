package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.SpaceItem

interface SpaceRepository {
    fun getAllSpace(): List<SpaceItem>
}
