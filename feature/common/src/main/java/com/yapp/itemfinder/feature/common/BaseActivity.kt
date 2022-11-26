package com.yapp.itemfinder.feature.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

@AndroidEntryPoint
abstract class BaseActivity<VM: BaseViewModel, VB: ViewBinding>: AppCompatActivity() {

    abstract val vm: VM

    abstract val binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initState()
    }

    open fun initState() {
        initViews()
        vm.fetchData()
        observeData()
    }

    abstract fun initViews()

    abstract fun observeData(): Job

    override fun onDestroy() {
        super.onDestroy()
    }

}
