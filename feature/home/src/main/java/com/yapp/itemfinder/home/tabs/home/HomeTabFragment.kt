package com.yapp.itemfinder.home.tabs.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.feature.common.BaseFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.home.databinding.FragmentHomeTabBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeTabFragment: BaseFragment<HomeTabViewModel, FragmentHomeTabBinding>() {

    override val vm by viewModels<HomeTabViewModel>()

    override val binding by viewBinding(FragmentHomeTabBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    override fun initViews() = with(binding) {
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
        }
    }

    override fun observeData(): Job = lifecycleScope.launch {
        vm.homeTabStateFlow.onEach {
            when (it) {
                is HomeTabState.Uninitialized -> Unit
                is HomeTabState.Loading -> handleLoading(it)
                is HomeTabState.Success -> handleSuccess(it)
                is HomeTabState.Error -> handleError(it)
            }
        }.launchIn(this)
    }

    private fun handleLoading(homeTabState: HomeTabState.Loading) {

    }

    private fun handleSuccess(homeTabState: HomeTabState.Success) {
        dataListAdapter?.submitList(homeTabState.dataList)
    }

    private fun handleError(homeTabState: HomeTabState.Error) {

    }

    companion object {

        val TAG = HomeTabFragment::class.simpleName.toString()

        fun newInstance() = HomeTabFragment()

    }

}