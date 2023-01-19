package com.yapp.itemfinder.home.tabs.home

import android.app.Activity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
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
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.visible
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
                    override fun getSpanSize(position: Int): Int =
                        dataListWithSpan[position].span
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
                        is HomeTabState.Loading -> handleLoading()
                        is HomeTabState.Success -> handleSuccess(state)
                        is HomeTabState.Empty -> handleEmpty()
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
        setFragmentResult(
            LockerListFragment.SPACE_ID_REQUEST_KEY,
            bundleOf(LockerListFragment.SPACE_ID_KEY to space.id)
        )
        when (activity) {
            is HomeActivity -> (activity as HomeActivity).addFragmentBackStack(LockerListFragment.TAG)
        }
    }


    private fun moveSpaceManage() {
        when (activity) {
            is HomeActivity -> (activity as HomeActivity).addFragmentBackStack(ManageSpaceFragment.TAG)
        }
    }

    private fun moveLockerDetail(locker: LockerEntity) {
        when (activity) {
            is HomeActivity -> (activity as HomeActivity).addFragmentBackStack(LockerDetailFragment.TAG
            , bundle = Bundle().apply { putParcelable("locker", locker) })
        }
    }

    private fun handleLoading() = with(binding) {
        emptyViewGroup.gone()
        progressBar.visible()
        recyclerView.gone()
    }

    private fun handleSuccess(homeTabState: HomeTabState.Success) {
        binding.emptyViewGroup.gone()
        binding.progressBar.gone()
        binding.recyclerView.visible()
        dataListWithSpan = homeTabState.dataListWithSpan
        dataBindHelper.bindList(dataListWithSpan.map { it.data }, vm)
        dataListAdapter?.submitList(dataListWithSpan.map { it.data })
        binding.recyclerView.addItemDecoration(
            SpaceItemDecoration(
                bottomFullSpacingDp = 16,
                horizontalHalfSpacingDp = 6,
                range = dataListWithSpan.indexOfFirst { it.data.type == CellType.SPACE_CELL }
                    ..dataListWithSpan.indexOfLast { it.data.type == CellType.SPACE_CELL }
            )
        )
    }

    private fun handleEmpty() = with(binding) {
        progressBar.gone()
        emptyViewGroup.visible()
        recyclerView.gone()
        emptySpaceAddButton.setOnClickListener {
            vm.moveSpaceManagementPage()
        }
    }

    private fun handleError(homeTabState: HomeTabState.Error) {
        binding.progressBar.gone()
    }

    companion object {

        val TAG = HomeTabFragment::class.simpleName.toString()

        fun newInstance() = HomeTabFragment()
    }
}
