package com.yapp.itemfinder.home.tabs.home

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import android.widget.Toast
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
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.feature.common.extension.visible
import com.yapp.itemfinder.feature.common.utility.DataWithSpan
import com.yapp.itemfinder.feature.common.utility.SpaceItemDecoration
import com.yapp.itemfinder.feature.home.R
import com.yapp.itemfinder.feature.home.databinding.FragmentHomeTabBinding
import com.yapp.itemfinder.home.HomeActivity
import com.yapp.itemfinder.space.LockerListFragment
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.space.lockerdetail.LockerDetailFragment
import com.yapp.itemfinder.space.managespace.AddSpaceDialog
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

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    private var addSpaceDialog: AddSpaceDialog? = null


    private val gridItemDecoration: SpaceItemDecoration by lazy {
        SpaceItemDecoration(
            bottomFullSpacingDp = 16,
            horizontalHalfSpacingDp = 6
        )
    }

    override fun initState() {
        super.initState()

        setFragmentResultListener(AddSpaceDialog.NEW_SPACE_REQUEST_KEY) { _, bundle ->
            val newSpaceName = bundle.getString(AddSpaceDialog.NEW_SPACE_NAME_BUNDLE_KEY)
            if (newSpaceName != null) {
                vm.createSpaceItem(newSpaceName)
            }
        }

        setFragmentResultListener(ManageSpaceFragment.NEW_SPACE_ADDED_REQUEST_KEY){ _, _ ->
            vm.fetchData()
        }
    }


    override fun initViews() = with(binding) {
        initToolBar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
        }
        recyclerView.adapter = dataListAdapter
        recyclerView.layoutManager = GridLayoutManager(activity, 2).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    dataListWithSpan[position].span
            }
        }
        recyclerView.addItemDecoration(gridItemDecoration)
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
                            if (sideEffect.message != null || sideEffect.msgResId != null) {
                                requireContext().showShortToast(
                                    sideEffect.message ?: getString(
                                        sideEffect.msgResId!!
                                    )
                                )
                            }
                        }
                        is HomeTabSideEffect.MoveSpacesManage -> {
                            moveSpaceManage(sideEffect)
                        }
                        is HomeTabSideEffect.MoveLockerDetail -> {
                            moveLockerDetail(sideEffect.locker)
                        }
                        is HomeTabSideEffect.ShowCreateNewSpacePopup -> {
                            if (addSpaceDialog == null) {
                                addSpaceDialog = AddSpaceDialog.newInstance()
                            }
                            addSpaceDialog?.show(
                                parentFragmentManager,
                                AddSpaceDialog.NEW_SPACE_REQUEST_KEY
                            )
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
        val bundle = Bundle()
        bundle.apply {
            putLong(LockerListFragment.SPACE_ID_KEY, space.id)
            putString(LockerListFragment.SPACE_NAME_KEY, space.name)
        }
        when (activity) {
            is HomeActivity -> (activity as HomeActivity).addFragmentBackStack(
                LockerListFragment.TAG,
                bundle
            )
        }
    }


    private fun moveSpaceManage(sideEffect: HomeTabSideEffect.MoveSpacesManage) {
        (requireActivity() as HomeActivity)
            .addFragmentBackStack(
                ManageSpaceFragment.TAG,
                bundle = bundleOf(
                    ManageSpaceFragment.MY_SPACE_TITLE_KEY to sideEffect.mySpaceUpperCellItem.title
                )
            )
    }

    private fun moveLockerDetail(locker: LockerEntity) {
        when (activity) {
            is HomeActivity -> (activity as HomeActivity).addFragmentBackStack(
                LockerDetailFragment.TAG,
                bundle = bundleOf(LockerDetailFragment.LOCKER_ENTITY_KEY to locker)
            )
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
        gridItemDecoration.range =
            dataListWithSpan.indexOfFirst { it.data.type == CellType.SPACE_CELL }..
        dataListWithSpan.indexOfLast { it.data.type == CellType.SPACE_CELL }
    }

    private fun handleEmpty() = with(binding) {
        progressBar.gone()
        emptyViewGroup.visible()
        recyclerView.gone()
        emptySpaceAddButton.setOnClickListener {
            vm.showCreateNewSpacePopup()
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
