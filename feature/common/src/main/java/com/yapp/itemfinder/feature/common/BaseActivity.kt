package com.yapp.itemfinder.feature.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.yapp.itemfinder.feature.common.extension.setStatusBarColor
import kotlinx.coroutines.Job

abstract class BaseActivity<VM: BaseViewModel, VB: ViewBinding>: AppCompatActivity() {

    abstract val vm: VM

    abstract val binding: VB

    private lateinit var fetchJob: Job

    abstract val depth: Depth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initState()
    }

    open fun initState() {
        setStatusBarColor(depth.colorId)
        initViews()
        fetchJob = vm.fetchData()
        observeData()
    }

    abstract fun initViews()

    abstract fun observeData(): Job

    override fun onDestroy() {
        super.onDestroy()
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }
    }

}
