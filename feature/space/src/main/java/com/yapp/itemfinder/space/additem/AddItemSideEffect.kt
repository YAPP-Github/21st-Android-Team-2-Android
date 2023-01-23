package com.yapp.itemfinder.space.additem

import com.yapp.itemfinder.feature.common.SideEffect

sealed class AddItemSideEffect : SideEffect {
    object OpenSelectCategoryDialog : AddItemSideEffect()
    object OpenDatePicker: AddItemSideEffect()
}
