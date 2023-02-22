package com.yapp.itemfinder.space.lockerdetail.itemfilter

import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.feature.common.State

sealed class ItemFilterState : State {
    object Uninitialized : ItemFilterState()

    object Loading : ItemFilterState()

    data class Success(
        val initialItemFilterCondition: ItemFilterCondition,
        val appliedItemFilterCondition: ItemFilterCondition,
        val tagList: List<Tag>,
        val isRefreshNeed: Boolean = false,
    ) : ItemFilterState()

    data class Error(
        val e: Throwable
    ) : ItemFilterState()
}
