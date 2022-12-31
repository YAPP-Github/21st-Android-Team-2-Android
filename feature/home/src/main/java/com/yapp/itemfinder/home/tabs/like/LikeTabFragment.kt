package com.yapp.itemfinder.home.tabs.like

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.home.databinding.FragmentLikeTabBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LikeTabFragment : BaseFragment<LikeTabViewModel, FragmentLikeTabBinding>() {

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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.likeTabStateFlow.onEach {
                        when (it) {
                            is LikeTabState.Uninitialized -> Unit
                            is LikeTabState.Loading -> handleLoading(it)
                            is LikeTabState.Success -> handleSuccess(it)
                            is LikeTabState.Error -> handleError(it)
                        }
                    }.launchIn(this)
                }
                launch {
                    vm.likeTabSideEffect.collect {

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
