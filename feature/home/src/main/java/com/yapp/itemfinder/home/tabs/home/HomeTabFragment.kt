package com.yapp.itemfinder.home.tabs.home

import android.app.Activity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.feature.common.utility.DataWithSpan
import com.yapp.itemfinder.feature.home.databinding.FragmentHomeTabBinding
import com.yapp.itemfinder.home.HomeActivity
import com.yapp.itemfinder.home.lockerlist.LockerListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeTabFragment : BaseStateFragment<HomeTabViewModel, FragmentHomeTabBinding>() {

    override val vm by viewModels<HomeTabViewModel>()

    override val binding by viewBinding(FragmentHomeTabBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    lateinit var dataListWithSpan: List<DataWithSpan<Data>>

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    private val activity: Activity by lazy { requireActivity() }

    override fun initViews() = with(binding) {
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
            recyclerView.layoutManager = GridLayoutManager(activity, 2).apply {

                spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {

                        return dataListWithSpan[position].span
                    }
                }
            }
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
                vm.sideEffectFlow.collect { sideEffect ->
                    when (sideEffect) {
                        is HomeTabSideEffect.MoveSpaceDetail -> {
                            moveSpaceDetail(sideEffect.space)
                        }
                        is HomeTabSideEffect.ShowToast -> {
                        }
                        is HomeTabSideEffect.MoveSpacesManage -> {
                            moveSpaceManage()
                        }
                    }
                }
            }
        }
    }

    private fun moveSpaceDetail(space: SpaceItem) {
        when (activity) {
            is HomeActivity -> (activity as HomeActivity).addFragmentBackStack(LockerListFragment.TAG)
        }
    }
    private fun handleLoading(homeTabState: HomeTabState.Loading) {
    }

    private fun handleSuccess(homeTabState: HomeTabState.Success) {
        dataListWithSpan = homeTabState.dataListWithSpan
        dataBindHelper.bindList(dataListWithSpan.map { it.data }, vm)
        dataListAdapter?.submitList(dataListWithSpan.map { it.data })
    }

    private fun handleError(homeTabState: HomeTabState.Error) {
    }

    companion object {

        val TAG = HomeTabFragment::class.simpleName.toString()

        fun newInstance() = HomeTabFragment()
    }
}
