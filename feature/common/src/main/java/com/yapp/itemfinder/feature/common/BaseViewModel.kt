package com.yapp.itemfinder.feature.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    protected val jobs = mutableListOf<Job>()

    open fun fetchData(): Job = viewModelScope.launch {  }

    override fun onCleared() {
        jobs.forEach {
            if (it.isCancelled.not())
                it.cancel()
        }
        super.onCleared()
    }

}
