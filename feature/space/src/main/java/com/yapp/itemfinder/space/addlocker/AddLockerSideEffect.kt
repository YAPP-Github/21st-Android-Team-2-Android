package com.yapp.itemfinder.space.addlocker

import com.yapp.itemfinder.feature.common.SideEffect

sealed class AddLockerSideEffect : SideEffect {
    object OpenSelectSpace : AddLockerSideEffect()
    object UploadImage: AddLockerSideEffect()
}
