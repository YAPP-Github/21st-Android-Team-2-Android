package com.yapp.itemfinder.home

import androidx.annotation.IdRes
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.feature.common.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class HomeViewModel: BaseViewModel() {

    private val _navigationItemStateFlow = MutableSharedFlow<MainNavigation?>()
    val navigationItemStateFlow: SharedFlow<MainNavigation?> = _navigationItemStateFlow

    fun changeNavigation(mainNavigation: MainNavigation) = viewModelScope.launch {
        _navigationItemStateFlow.emit(mainNavigation)
    }

}

class MainNavigation(@IdRes val navigationMenuId: Int)
