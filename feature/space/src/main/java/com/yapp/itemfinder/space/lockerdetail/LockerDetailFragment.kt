package com.yapp.itemfinder.space.lockerdetail

import android.os.Bundle
import android.util.Log
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
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.FragmentNavigator
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

    override fun initViews() = with(binding) {
        initToolBar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
        }

        initBottomSheet()
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

        ViewCompat.setOnApplyWindowInsetsListener(requireView()) { v, insets ->
            val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // View 계층에 반영될 Inset들을 반환한다.
            binding.lockerDetailImageView.post {
                behavior.maxHeight =
                    binding.root.measuredHeight - binding.toolbar.measuredHeight - inset.top
                behavior.peekHeight =
                    (binding.root.measuredHeight
                            - binding.lockerDetailImageView.measuredHeight
                            - resources.getDimension(CR.dimen.collapsing_toolbar_container_height)
                            - inset.top
                            ).toInt()
            }
            insets
        }

        behavior.draggableView = binding.bottomSheet.itemsDraggableContainer

    }

    override fun observeData(): Job {
        Log.i("LockerDetailFragment", "observeData: ")
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
                bundle.putString(ItemDetailFragment.SPACE_NAME_KEY, "주방")
                bundle.putString(ItemDetailFragment.LOCKER_NAME_KEY, "냉장고")
                bundle.putLong(ItemDetailFragment.ITEM_ID_KEY, itemId)
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
        binding.defaultTopNavigationView.titleText = lockerDetailState.locker.name

        dataBindHelper.bindList(lockerDetailState.dataList, vm)
        dataListAdapter?.submitList(lockerDetailState.dataList)

        val itemCount: Int = dataListAdapter?.itemCount ?: 0
        binding.bottomSheet.totalItemCount.text = getString(string.totalCount, itemCount)
        binding.floatingActionButton.setOnClickListener { vm.moveItemDetail(1L) } // 각 물건별로 동작하도록 Item, ItemSimpleViewHolder 쪽에 핸들러 설정이 필요합니다.
    }

    companion object {

        val TAG = LockerDetailFragment::class.simpleName.toString()

        const val LOCKER_ENTITY_KEY = "LOCKER_ENTITY_KEY"

        fun newInstance() = LockerDetailFragment()
    }
}
