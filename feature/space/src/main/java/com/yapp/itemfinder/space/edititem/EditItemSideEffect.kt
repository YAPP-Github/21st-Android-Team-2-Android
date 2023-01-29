package com.yapp.itemfinder.space.edititem

import com.yapp.itemfinder.feature.common.SideEffect

sealed class EditItemSideEffect : SideEffect {
    object OpenSelectCategoryDialog : EditItemSideEffect()
    object OpenExpirationDatePicker : EditItemSideEffect()
    object OpenPurchaseDatePicker : EditItemSideEffect()
    object FillOutRequiredSnackBar : EditItemSideEffect()
    object FillOutNameSnackBar : EditItemSideEffect()
    object FillOutCategorySnackBar : EditItemSideEffect()
    object FillOutLocationSnackBar : EditItemSideEffect()
    object NameLengthLimitSnackBar : EditItemSideEffect()
    object MemoLengthLimitSnackBar : EditItemSideEffect()
}
