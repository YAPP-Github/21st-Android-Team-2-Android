package com.yapp.itemfinder.space.lockerdetail

import android.annotation.SuppressLint
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
        blockBottomSheetTouchIntercept()
    }

    /**
     * 리사이클러뷰 부모가 터치이벤트를 인터셉트하는 것을 방지합니다. 리사이클러뷰 스크롤을 해야 하는데,
     */

    @SuppressLint("ClickableViewAccessibility")
    private fun setBottomSheetBehavior() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet.root) as CustomDraggableBottomSheetBehaviour
        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                with(binding.bottomSheet.toggleImageView) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            setImageResource(R.drawable.toggle_down)
                            binding.appBar.setExpanded(true, false)
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            setImageResource(R.drawable.toggle_up)
                            binding.appBar.setExpanded(false, false)
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
                behavior.maxHeight = binding.root.measuredHeight - binding.toolbar.measuredHeight - inset.top
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


        /*binding.bottomSheet.recyclerview.setOnTouchListener { _, e ->
            when(e.action) {
                MotionEvent.ACTION_DOWN -> {
                    behavior.isDraggable = false
                    return@setOnTouchListener false
                }
                MotionEvent.ACTION_MOVE -> {
                    behavior.isDraggable = false
                    return@setOnTouchListener false
                }
                MotionEvent.ACTION_UP -> {
                    behavior.isDraggable = true
                    return@setOnTouchListener false
                }
                else -> false
            }
            return@setOnTouchListener true
        }*/
    }

    private fun blockBottomSheetTouchIntercept() {

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
        binding.defaultTopNavigationView.titleText = lockerDetailState.locker.name

        dataBindHelper.bindList(lockerDetailState.dataList, vm)
        dataListAdapter?.submitList(lockerDetailState.dataList)

        val itemCount: Int = dataListAdapter?.itemCount ?: 0
        binding.bottomSheet.totalItemCount.text = getString(string.totalCount, itemCount)
    }

//    private fun handleError(lockerListState: LockerListState.Error) {
//
//    }

    companion object {

        val TAG = LockerDetailFragment::class.simpleName.toString()

        const val LOCKER_ENTITY_KEY = "LOCKER_ENTITY_KEY"

        fun newInstance() = LockerDetailFragment()
    }
}
