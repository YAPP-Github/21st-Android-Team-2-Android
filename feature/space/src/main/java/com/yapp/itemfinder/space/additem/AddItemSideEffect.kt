package com.yapp.itemfinder.space.additem

import com.yapp.itemfinder.domain.model.SpaceAndLockerEntity
import com.yapp.itemfinder.feature.common.SideEffect

sealed class AddItemSideEffect : SideEffect {
    object OpenSelectCategoryDialog : AddItemSideEffect()
    object OpenExpirationDatePicker : AddItemSideEffect()
    object OpenPurchaseDatePicker : AddItemSideEffect()
    object FillOutRequiredSnackBar : AddItemSideEffect()
    object FillOutNameSnackBar : AddItemSideEffect()
    object FillOutCategorySnackBar : AddItemSideEffect()
    object FillOutLocationSnackBar : AddItemSideEffect()
    object NameLengthLimitSnackBar : AddItemSideEffect()
    object MemoLengthLimitSnackBar : AddItemSideEffect()

    data class MoveSelectSpace(
        val spaceAndLockerEntity: SpaceAndLockerEntity? = null
    ) : AddItemSideEffect()

}
