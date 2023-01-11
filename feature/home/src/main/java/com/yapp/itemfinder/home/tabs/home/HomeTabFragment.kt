package com.yapp.itemfinder.home.tabs.home

import android.app.Activity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.yapp.itemfinder.domain.model.CellType
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.utility.DataWithSpan
import com.yapp.itemfinder.feature.common.utility.SpaceItemDecoration
import com.yapp.itemfinder.feature.home.databinding.FragmentHomeTabBinding
import com.yapp.itemfinder.home.HomeActivity
import com.yapp.itemfinder.space.LockerListFragment
import com.yapp.itemfinder.space.lockerdetail.LockerDetailFragment
import com.yapp.itemfinder.space.managespace.ManageSpaceFragment
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
                        is HomeTabSideEffect.MoveLockerDetail -> {
                            moveLockerDetail(sideEffect.locker)
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


    private fun moveSpaceManage(){
        when (activity) {
            is HomeActivity -> (activity as HomeActivity).addFragmentBackStack(ManageSpaceFragment.TAG)
        }
    }

    private fun moveLockerDetail(locker: Locker) {
        when (activity) {
            is HomeActivity -> (activity as HomeActivity).addFragmentBackStack(LockerDetailFragment.TAG
            , bundlePair = "locker" to locker)
        }
    }

    private fun handleLoading(homeTabState: HomeTabState.Loading) {
    }

    private fun handleSuccess(homeTabState: HomeTabState.Success) {
        dataListWithSpan = homeTabState.dataListWithSpan
        dataBindHelper.bindList(dataListWithSpan.map { it.data }, vm)
        dataListAdapter?.submitList(dataListWithSpan.map { it.data })
        binding.recyclerView.addItemDecoration(
            SpaceItemDecoration(
                bottomFullSpacingDp = 16,
                horizontalHalfSpacingDp = 6,
                range = dataListWithSpan.indexOfFirst { it.data.type == CellType.SPACE_CELL }..dataListWithSpan.indexOfLast { it.data.type == CellType.SPACE_CELL }
            )
        )
    }

    private fun handleError(homeTabState: HomeTabState.Error) {
    }

    companion object {

        val TAG = HomeTabFragment::class.simpleName.toString()

        fun newInstance() = HomeTabFragment()
    }
}
