package com.yapp.itemfinder.home

import androidx.annotation.IdRes
import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.feature.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: BaseViewModel() {

    private val _navigationItemStateFlow = MutableStateFlow<MainNavigation?>(null)
    val navigationItemStateFlow: StateFlow<MainNavigation?> = _navigationItemStateFlow

    fun changeNavigation(mainNavigation: MainNavigation) = viewModelScope.launch {
        _navigationItemStateFlow.value = mainNavigation
    }

}

class MainNavigation(@IdRes val navigationMenuId: Int)
