package com.yapp.itemfinder.feature.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

abstract class BaseActivity<VM: BaseViewModel, VB: ViewBinding>: AppCompatActivity() {

    abstract val viewModel: VM

    abstract val binding: VB

    private lateinit var fetchJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initState()
    }

    open fun initState() {
        initViews()
        fetchJob = viewModel.fetchData()
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
