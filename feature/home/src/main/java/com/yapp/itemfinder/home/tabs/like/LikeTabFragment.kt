package com.yapp.itemfinder.home.tabs.like

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.home.databinding.FragmentLikeTabBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LikeTabFragment : BaseStateFragment<LikeTabViewModel, FragmentLikeTabBinding>() {

    override val vm by viewModels<LikeTabViewModel>()

    override val binding by viewBinding(FragmentLikeTabBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
        }
    }

    override fun observeData(): Job {
        val job = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.stateFlow.collect { state ->
                        when (state) {
                            is LikeTabState.Uninitialized -> Unit
                            is LikeTabState.Loading -> handleLoading(state)
                            is LikeTabState.Success -> handleSuccess(state)
                            is LikeTabState.Error -> handleError(state)
                        }
                    }
                }
                launch {
                    vm.sideEffectFlow.collect { sideEffect ->
                        when (sideEffect) {
                            is LikeTabSideEffect.ShowToast -> {
                                Toast.makeText(requireContext(), sideEffect.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        return job
    }

    private fun handleLoading(likeTabState: LikeTabState) {

    }

    private fun handleSuccess(likeTabState: LikeTabState.Success) {
        dataBindHelper.bindList(likeTabState.dataList, vm)
        dataListAdapter?.submitList(likeTabState.dataList)
    }

    private fun handleError(likeTabState: LikeTabState.Error) {

    }

    companion object {

        val TAG = LikeTabFragment::class.simpleName.toString()

        fun newInstance() = LikeTabFragment()

    }

}
