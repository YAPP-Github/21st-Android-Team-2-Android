package com.yapp.itemfinder.space.lockerdetail

import android.os.Bundle
import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.model.ScreenMode
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.FragmentNavigator
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.R.string
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.*
import com.yapp.itemfinder.feature.common.views.behavior.CustomDraggableBottomSheetBehaviour
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.addlocker.AddLockerActivity
import com.yapp.itemfinder.space.databinding.FragmentLockerDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.space.itemdetail.ItemDetailFragment

@AndroidEntryPoint
class LockerDetailFragment :
    BaseStateFragment<LockerDetailViewModel, FragmentLockerDetailBinding>() {

    override val vm by viewModels<LockerDetailViewModel>()

    override val depth: Depth
        get() = Depth.SECOND

    override val binding by viewBinding(FragmentLockerDetailBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var inset: Insets? = null
    override fun initViews() = with(binding) {
        ViewCompat.setOnApplyWindowInsetsListener(requireView()) { v, insets ->
            inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // View 계층에 반영될 Inset들을 반환한다.
            insets
        }

        initToolBar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
        }

        initBottomSheet()
        handleRecyclerViewListener()
        setResultLauncher()
    }

    private fun initToolBar() = with(binding.defaultTopNavigationView) {
        backButtonImageResId = CR.drawable.ic_back
        backButtonClickListener = {
            onBackPressedCallback.handleOnBackPressed()
        }
        containerColor = depth.colorId

        rightFirstIcon = CR.drawable.ic_search
        rightFirstIconClickListener = {
            Toast.makeText(requireContext(), "정렬 버튼 클릭", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initBottomSheet() {
        with(binding.bottomSheet) {
            recyclerview.adapter = dataListAdapter
            recyclerview.addItemDecoration(
                MaterialDividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                ).apply {
                    isLastItemDecorated = false
                    dividerColor =
                        requireContext().getColor(CR.color.gray_01)
                }
            )

            val screenRatio = requireContext().screenHeight() / requireContext().screenWidth().toFloat()
            recyclerview.updatePadding(
                bottom = if (screenRatio > 2) {
                    requireContext().dimen(CR.dimen.bottom_sheet_bottom_padding_large).toInt()
                } else {
                    requireContext().dimen(CR.dimen.bottom_sheet_bottom_padding_default).toInt()
                }
            )
        }

        setBottomSheetBehavior()
    }

    private fun setBottomSheetBehavior() {
        fun setFilterActive(isActive: Boolean) = with(binding) {
            orderButton.isClickable = isActive
            conditionButton.isClickable = isActive
            tagButton.isClickable = isActive
        }

        val behavior =
            BottomSheetBehavior.from(binding.bottomSheet.root) as CustomDraggableBottomSheetBehaviour
        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                with(binding.bottomSheet.toggleImageView) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            setImageResource(R.drawable.toggle_down)
                            setFilterActive(false)
                        }
                        BottomSheetBehavior.STATE_SETTLING,
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            setFilterActive(false)
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            setImageResource(R.drawable.toggle_up)
                            setFilterActive(true)
                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            TODO()
                        }
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            TODO()
                        }
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                return
            }
        })

        setBottomSheetPeekHeight(isExpand = true)

        behavior.draggableView = binding.bottomSheet.itemsDraggableContainer

    }

    private fun setBottomSheetPeekHeight(isExpand: Boolean, isAnimate: Boolean = false) {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet.root) as CustomDraggableBottomSheetBehaviour
        binding.itemsMarkerMapView.post {
            val toolBarInset = inset?.top ?: 0
            val toolbarContainerHeight = requireContext().dimen(CR.dimen.collapsing_toolbar_container_height).toInt()
            val toolbarHeight = binding.toolbar.measuredHeight
            behavior.maxHeight = binding.root.measuredHeight - binding.toolbar.measuredHeight - toolBarInset
            val peekHeight = (binding.root.measuredHeight
                - binding.itemsMarkerMapView.getImageHeight()
                - (if (isExpand) toolbarContainerHeight else toolbarHeight)
                - toolBarInset)
            behavior.setPeekHeight(peekHeight, isAnimate)
        }
    }

    private fun handleRecyclerViewListener() = with(binding.bottomSheet.recyclerview) {
        var filterCollapseAnimator: Animator? = null
        var filterExpandAnimator: Animator? = null
        var isFilterCollapsed = false
        var isFilterExpanded = false
        var isInitialized = false

        fun isAnimateRunning() =
            filterExpandAnimator?.isRunning == true
                || filterCollapseAnimator?.isRunning == true

        fun startChangeFilterShape(startHeight: Int, endHeight: Int, isExpand: Boolean, isAnimate: Boolean = true) {
            val animator = ValueAnimator.ofInt(startHeight, endHeight)
                .setDuration(if (isAnimate) BOTTOM_SHEET_TRANSITION_DURATION else 0).apply {
                    addUpdateListener { animation ->
                        val value = animation.animatedValue as Int
                        binding.filterContainer.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                            height = value
                        }
//                        binding.itemsMarkerMapView.updateLayoutParams<ConstraintLayout.LayoutParams> {
//                            topMargin = value
//                        }
                    }
                    start()
                    isFilterExpanded = isExpand
                    isFilterCollapsed = isExpand.not()
                }
            if (isExpand) {
                filterExpandAnimator = animator
            } else {
                filterCollapseAnimator = animator
            }
        }

        fun handleExpandFilter(isExpand: Boolean, isAnimate: Boolean = true) {
            post {
                val toolbarContainerHeight = requireContext().dimen(CR.dimen.collapsing_toolbar_container_height).toInt()
                val toolbarHeight = binding.toolbar.measuredHeight

                if (isExpand) {
                    if (isAnimateRunning() || isFilterExpanded) return@post
                    startChangeFilterShape(toolbarHeight, toolbarContainerHeight, isExpand = true, isAnimate = isAnimate)
                    setBottomSheetPeekHeight(isExpand = true, isAnimate = isAnimate)
                } else {
                    if (isAnimateRunning() || isFilterCollapsed) return@post
                    startChangeFilterShape(toolbarContainerHeight, toolbarHeight, isExpand = false, isAnimate = isAnimate)
                    setBottomSheetPeekHeight(isExpand = false, isAnimate = isAnimate)
                }
            }
        }

        fun focusCurrentVisibleItem() {
            val firstVisibleItemPosition =
                (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
                vm.applyFocusFirstItem(
                    if (!canScrollVertically(1)) {
                        requireNotNull(adapter).itemCount - 1
                    } else {
                        firstVisibleItemPosition
                    }
                )
            }
        }

        setOnScrollChangeListener { _: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            when {
                isInitialized.not() -> handleExpandFilter(true, isAnimate = false)
                scrollState == RecyclerView.SCROLL_STATE_SETTLING -> {
                    if (oldScrollY in 0..10) {
                        handleExpandFilter(true)
                    } else {
                        handleExpandFilter(scrollY < oldScrollY)
                    }
                }

            }
            focusCurrentVisibleItem()
            isInitialized = true
        }
    }

    override fun observeData(): Job {
        val job = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.stateFlow.collect { state ->
                        when (state) {
                            is LockerDetailState.Uninitialized -> Unit
                            is LockerDetailState.Loading -> handleLoading()
                            is LockerDetailState.Success -> handleSuccess(state)
                            is LockerDetailState.Error -> Unit
                        }
                    }
                }
                launch {
                    vm.sideEffectFlow.collect { sideEffect ->
                        when (sideEffect) {
                            is LockerDetailSideEffect.MoveItemDetail -> {
                                moveItemDetail(itemId = sideEffect.itemId)
                            }
                        }
                    }
                }
            }
        }
        return job
    }

    private fun moveItemDetail(itemId: Long) {
        when (val activity = requireActivity()) {
            is FragmentNavigator -> {
                val bundle = Bundle()
                bundle.apply {
                    putString(ItemDetailFragment.SPACE_NAME_KEY, "주방")
                    putString(ItemDetailFragment.LOCKER_NAME_KEY, "냉장고")
                    putLong(ItemDetailFragment.ITEM_ID_KEY, itemId)
                }
                activity.addFragmentBackStack(
                    ItemDetailFragment.TAG,
                    bundle = bundle
                )
            }
        }
    }

    private fun handleLoading() {

    }

    private fun handleSuccess(lockerDetailState: LockerDetailState.Success) {
        if (lockerDetailState.needToFetch.not()) {
            lockerDetailState.lastFocusedItem?.let {
                binding.itemsMarkerMapView.applyFocusMarker(it)
            }
            return
        }

        val dataList = lockerDetailState.dataList
        if (lockerDetailState.locker.imageUrl != null) {
            handleItemMarkers(lockerDetailState.locker , dataList.map { it as Item })
            binding.emptyMarkerMapGroup.gone()
        } else {
            binding.emptyMarkerMapGroup.visible()
            binding.emptySpaceAddButton.setOnClickListener {
                val intent = AddLockerActivity.newIntent(requireActivity()).apply {
                    putExtra(
                        AddLockerActivity.LOCKER_ENTITY_KEY,
                        lockerDetailState.locker
                    )
                    putExtra(
                        AddLockerActivity.SPACE_ID_KEY,
                        lockerDetailState.locker.spaceId
                    )
                    putExtra(
                        AddLockerActivity.SCREEN_MODE,
                        ScreenMode.EDIT_MODE.label
                    )
                }
                resultLauncher.launch(intent)
            }
        }

        binding.defaultTopNavigationView.titleText = lockerDetailState.locker.name

        dataBindHelper.bindList(dataList, vm)
        dataListAdapter?.submitList(dataList)

        val itemCount: Int = dataListAdapter?.itemCount ?: 0
        binding.bottomSheet.totalItemCount.text = getString(string.totalCount, itemCount)
        //binding.floatingActionButton.setOnClickListener { vm.moveItemDetail(1L) } // 각 물건별로 동작하도록 Item, ItemSimpleViewHolder 쪽에 핸들러 설정이 필요합니다.
        binding.bottomSheet.totalItemCount.text = getString(string.totalCount, dataList.size)
    }

    private fun handleItemMarkers(lockerEntity: LockerEntity, items: List<Item>) = with(binding) {
        itemsMarkerMapView.fetchItems(lockerEntity, items)
    }

    private fun setResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    vm.reFetchData()
                }
            }
    }

    companion object {

        val TAG = LockerDetailFragment::class.simpleName.toString()

        const val LOCKER_ENTITY_KEY = "LOCKER_ENTITY_KEY"

        const val BOTTOM_SHEET_TRANSITION_DURATION = 150L

        fun newInstance() = LockerDetailFragment()
    }
}
