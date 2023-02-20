package com.yapp.itemfinder.feature.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.yapp.itemfinder.feature.common.extension.setStatusBarColor
import kotlinx.coroutines.Job

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {

    /**
     * Fragment에서 onBackPressed 이벤트가 발생했을 때 처리할 동작을 구현합니다.
     * @return [onBackPressedAction]] 이벤트를 처리했으면 true를 반환합니다.
     */
    open fun onBackPressedAction(): Boolean {
        return false
    }

    protected open fun backPressed() {
        (requireActivity() as? BaseActivity<*, *>)?.onBackPressedCallback?.handleOnBackPressed()
    }

    abstract val vm: VM

    abstract val binding: VB

    private lateinit var fetchJob: Job

    abstract val depth: Depth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initState()
    }

    open fun initState() {
        setStatusBarColor(depth.colorId)
        initViews()
        fetchJob = vm.fetchData()
        observeData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        /**
         * Apply Status bar color when pop backstack
         */
        if (hidden.not()) setStatusBarColor(depth.colorId)
    }

    abstract fun initViews()

    abstract fun observeData(): Job

    override fun onDestroyView() {
        super.onDestroyView()
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }
    }

}
