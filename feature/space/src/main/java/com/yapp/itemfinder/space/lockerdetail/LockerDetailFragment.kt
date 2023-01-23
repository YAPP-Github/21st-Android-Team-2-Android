package com.yapp.itemfinder.space.lockerdetail

import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.databinding.FragmentLockerDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.feature.common.R.string
import com.yapp.itemfinder.feature.common.views.behavior.CustomDraggableBottomSheetBehaviour

@AndroidEntryPoint
class LockerDetailFragment : BaseStateFragment<LockerDetailViewModel, FragmentLockerDetailBinding>() {

    override val vm by viewModels<LockerDetailViewModel>()

    override val depth: Depth
        get() = Depth.SECOND

    override val binding by viewBinding(FragmentLockerDetailBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        initToolBar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
        }

        initBottomSheet()
        handleRecyclerViewListener()
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
                        requireContext().getColor(com.yapp.itemfinder.feature.common.R.color.gray_01)
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

        val behavior = BottomSheetBehavior.from(binding.bottomSheet.root) as CustomDraggableBottomSheetBehaviour
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

        ViewCompat.setOnApplyWindowInsetsListener(requireView()) { v, insets ->
            val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // View 계층에 반영될 Inset들을 반환한다.
            binding.itemsMarkerMapView.post {
                behavior.maxHeight = binding.root.measuredHeight - binding.toolbar.measuredHeight - inset.top
                behavior.peekHeight =
                    (binding.root.measuredHeight
                        - binding.itemsMarkerMapView.measuredHeight
                        - resources.getDimension(CR.dimen.collapsing_toolbar_container_height)
                        - inset.top
                        ).toInt()
            }
            insets
        }

        behavior.draggableView = binding.bottomSheet.itemsDraggableContainer

    }

    private fun handleRecyclerViewListener() = with(binding.bottomSheet.recyclerview) {
        fun focusCurrentVisibleItem() {
            val firstVisibleItemPosition =
                (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            vm.applyFocusFirstItem(
                if (!canScrollVertically(1)) {
                    requireNotNull(adapter).itemCount - 1
                } else {
                    firstVisibleItemPosition
                }
            )
        }

        setOnScrollChangeListener { _: View?, _: Int, _: Int, _: Int, _: Int -> focusCurrentVisibleItem() }
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
//
                    }
                }
            }
        }
        return job
    }

    private fun handleLoading() {

    }

    private fun handleSuccess(lockerDetailState: LockerDetailState.Success) {
        if (lockerDetailState.needToFetch.not()) return
        handleItemMarkers(lockerDetailState.dataList.filterIsInstance<Item>())

        binding.defaultTopNavigationView.titleText = lockerDetailState.locker.name

        dataBindHelper.bindList(lockerDetailState.dataList, vm)
        dataListAdapter?.submitList(lockerDetailState.dataList)

        val itemCount: Int = dataListAdapter?.itemCount ?: 0
        binding.bottomSheet.totalItemCount.text = getString(string.totalCount, itemCount)
    }

    private fun handleItemMarkers(items: List<Item>) = with(binding) {
        itemsMarkerMapView.fetchItems(items, items.first())
    }

    companion object {

        val TAG = LockerDetailFragment::class.simpleName.toString()

        const val LOCKER_ENTITY_KEY = "LOCKER_ENTITY_KEY"

        fun newInstance() = LockerDetailFragment()
    }
}
