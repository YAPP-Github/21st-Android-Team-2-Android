package com.yapp.itemfinder.space.lockerdetail

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LockerDetailViewModelFactory(val args: Bundle): ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LockerDetailViewModel::class.java))
            return LockerDetailViewModel(args) as T
        else throw IllegalArgumentException("unknwon viewmodel class")
    }
}
