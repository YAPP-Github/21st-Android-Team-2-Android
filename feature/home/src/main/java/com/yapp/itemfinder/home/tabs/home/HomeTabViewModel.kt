package com.yapp.itemfinder.home.tabs.home

import androidx.lifecycle.viewModelScope
import com.yapp.itemfinder.feature.common.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeTabViewModel: BaseViewModel() {

    val homeTabStateFlow = MutableStateFlow<HomeTabState>(HomeTabState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        setState(HomeTabState.Loading)
        // call API
        setState(
            HomeTabState.Success(
                dataList = listOf()
            )
        )
        // Error
        setState(
            HomeTabState.Error(Exception())
        )
    }

    private fun setState(state: HomeTabState) {
        homeTabStateFlow.value = state
    }

}
