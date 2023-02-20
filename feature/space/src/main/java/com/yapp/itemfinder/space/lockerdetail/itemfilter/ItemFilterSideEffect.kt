package com.yapp.itemfinder.space.lockerdetail.itemfilter

import com.yapp.itemfinder.feature.common.SideEffect

sealed class ItemFilterSideEffect : SideEffect {

    data class ShowToast(val message: String) : ItemFilterSideEffect()

    data class Reset(
        val initialItemFilterCondition: ItemFilterCondition
    ) : ItemFilterSideEffect()

    data class Apply(
        val appliedItemFilterCondition: ItemFilterCondition
    ) : ItemFilterSideEffect()

}
