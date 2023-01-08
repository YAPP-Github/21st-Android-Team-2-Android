package com.yapp.itemfinder.domain.usecase

import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.domain.repository.SpaceRepository

class GetSpacesUseCase(private val spaceRepository: SpaceRepository) {

    operator fun invoke(): List<SpaceItem> {
        return spaceRepository.getAllSpace()
    }
}