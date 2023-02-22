package com.yapp.itemfinder.feature.common

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.yapp.itemfinder.feature.common.extension.setStatusBarColor
import kotlinx.coroutines.Job

abstract class BaseActivity<VM: BaseViewModel, VB: ViewBinding>: AppCompatActivity() {

    abstract val vm: VM

    abstract val binding: VB

    private lateinit var fetchJob: Job

    abstract val depth: Depth

    lateinit var onBackPressedCallback: OnBackPressedCallback

    protected open fun onBackPressedAction() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initState()
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!handleFragmentOnBackPressed()) {
                    onBackPressedAction()
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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

    /**
     * 현재 back stack에 포함된 Fragment들의 onBackPressed 이벤트를 호출합니다.
     * @return onBackPressed 이벤트를 처리한 Fragment가 있으면 true를 반환합니다.
     */
    private fun handleFragmentOnBackPressed(): Boolean {
        val fragmentManager = supportFragmentManager
        val backStackEntryCount = fragmentManager.backStackEntryCount
        for (i in backStackEntryCount - 1 downTo 0) {
            val backStackEntry = fragmentManager.getBackStackEntryAt(i)
            val fragmentTag = backStackEntry.name
            val fragment = fragmentManager.findFragmentByTag(fragmentTag)
            if (fragment is BaseFragment<*, *>) {
                if (fragment.onBackPressedAction()) {
                    return true
                }
            }
        }
        return false
    }

}
