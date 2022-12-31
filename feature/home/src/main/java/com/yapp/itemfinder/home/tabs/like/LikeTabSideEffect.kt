package com.yapp.itemfinder.home.tabs.like

sealed class LikeTabSideEffect {

    data class ShowToast(val message: String): LikeTabSideEffect()

}