package com.yapp.itemfinder.home.tabs.home

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.home.databinding.FragmentHomeTabBinding
import com.yapp.itemfinder.home.HomeActivity
import com.yapp.itemfinder.locker.LockerListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeTabFragment : BaseStateFragment<HomeTabViewModel, FragmentHomeTabBinding>() {

    override val vm by viewModels<HomeTabViewModel>()

    override val binding by viewBinding(FragmentHomeTabBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
        }
    }

    override fun observeData(): Job = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                vm.stateFlow.collect { state ->
                    when (state) {
                        is HomeTabState.Uninitialized -> Unit
                        is HomeTabState.Loading -> handleLoading(state)
                        is HomeTabState.Success -> handleSuccess(state)
                        is HomeTabState.Error -> handleError(state)
                    }
                }
            }
            launch {
                vm.sideEffectFlow.collect{ sideEffect ->
                    when(sideEffect){
                        is HomeTabSideEffect.MoveSpaceDetail -> {
                            moveSpaceDetail(sideEffect.space)
                        }
                        is HomeTabSideEffect.ShowToast -> {

                        }
                    }
                }
            }

        }
    }

    private fun moveSpaceDetail(space: SpaceItem){
        val activity = requireActivity()
        when (activity){
            is HomeActivity -> activity.showFragment(LockerListFragment.TAG)
        }
    }

    private fun handleLoading(homeTabState: HomeTabState.Loading) {
    }

    private fun handleSuccess(homeTabState: HomeTabState.Success) {
        dataBindHelper.bindList(homeTabState.dataList, vm)
        dataListAdapter?.submitList(homeTabState.dataList)
        homeTabState.dataList.forEach { data ->
            data.handler =
                { data -> Toast.makeText(requireContext(), "버튼 클릭", Toast.LENGTH_SHORT).show(); }
        }
    }

    private fun handleError(homeTabState: HomeTabState.Error) {
    }

    companion object {

        val TAG = HomeTabFragment::class.simpleName.toString()

        fun newInstance() = HomeTabFragment()
    }
}
