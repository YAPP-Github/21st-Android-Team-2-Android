package com.yapp.itemfinder.feature.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.yapp.itemfinder.feature.common.extension.setStatusBarColor
import kotlinx.coroutines.Job

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {

    protected lateinit var onBackPressedCallback: OnBackPressedCallback

    protected open fun onBackPressedAction() {
        parentFragmentManager.popBackStack()
    }

    abstract val vm: VM

    abstract val binding: VB

    private lateinit var fetchJob: Job

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = binding.root

    abstract val depth: Depth

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

    enum class Depth(@ColorRes val colorId: Int) {
        FIRST(R.color.white), SECOND(R.color.brown_02), THIRD(R.color.brown_03);
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        /**
         * Apply Status bar color when pop backstack
         */
        if (hidden.not()) setStatusBarColor(depth.colorId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedAction()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressedCallback.remove()
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
