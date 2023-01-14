package com.yapp.itemfinder.home.tabs.home

import android.widget.Toast
import androidx.core.os.bundleOf
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
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.visible
import com.yapp.itemfinder.feature.common.utility.DataWithSpan
import com.yapp.itemfinder.feature.common.utility.SpaceItemDecoration
import com.yapp.itemfinder.feature.home.R
import com.yapp.itemfinder.feature.home.databinding.FragmentHomeTabBinding
import com.yapp.itemfinder.home.HomeActivity
import com.yapp.itemfinder.space.LockerListFragment
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.space.managespace.ManageSpaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeTabFragment : BaseStateFragment<HomeTabViewModel, FragmentHomeTabBinding>() {

    override val vm by viewModels<HomeTabViewModel>()

    override val binding by viewBinding(FragmentHomeTabBinding::inflate)

    override val depth: Depth
        get() = Depth.FIRST

    private var dataListAdapter: DataListAdapter<Data>? = null

    lateinit var dataListWithSpan: List<DataWithSpan<Data>>

    override fun onBackPressedAction() {
        requireActivity().finish()
    }

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        initToolBar()
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

    private fun initToolBar() = with(binding.searchTopNavigationView) {
        leftButtonImageResId = CR.drawable.ic_menu
        searchBarImageResId = CR.drawable.ic_search
        searchBarBackgroundResId = CR.drawable.bg_button_brown_02_radius_8
        searchBarText = getString(R.string.home_search_bar_text)
        searchBarTextColor = CR.color.gray_03
        leftButtonClickListener = {
            Toast.makeText(requireContext(), "메뉴버튼 클릭", Toast.LENGTH_SHORT).show()
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
                            moveSpaceManage(sideEffect)
                        }
                    }
                }
            }
        }
    }

    private fun moveSpaceDetail(spaceItem: SpaceItem) {
        (requireActivity() as HomeActivity)
            .addFragmentBackStack(
                LockerListFragment.TAG,
                arguments = bundleOf(
                    LockerListFragment.SPACE_ITEM_KEY to spaceItem
                )
            )
    }


    private fun moveSpaceManage(sideEffect: HomeTabSideEffect.MoveSpacesManage) {
        (requireActivity() as HomeActivity)
            .addFragmentBackStack(
                ManageSpaceFragment.TAG,
                arguments = bundleOf(
                    ManageSpaceFragment.MY_SPACE_TITLE_KEY to sideEffect.mySpaceUpperCellItem.title
                )
            )
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
            //vm.runSpaceManagementPage(it)
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
