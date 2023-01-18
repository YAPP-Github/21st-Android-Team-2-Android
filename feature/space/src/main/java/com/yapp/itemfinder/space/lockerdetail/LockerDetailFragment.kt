package com.yapp.itemfinder.space.lockerdetail

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.dpToPx
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.databinding.FragmentLockerDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.yapp.itemfinder.feature.common.R.string

@AndroidEntryPoint
class LockerDetailFragment :
    BaseStateFragment<LockerDetailViewModel, FragmentLockerDetailBinding>() {

    @Inject
    lateinit var lockerDetailViewModelFactory: LockerDetailViewModel.LockerIdAssistedFactory

    override val vm by viewModels<LockerDetailViewModel> {
        LockerDetailViewModel.provideFactory(
            lockerDetailViewModelFactory,
            (requireArguments().get("locker") as Locker).id
        )
    }

    override val binding by viewBinding(FragmentLockerDetailBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
        }

        initBottomSheet()
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

    private fun setBottomSheetBehavior() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        behavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                with(binding.bottomSheet.toggleImageView) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED)
                        setImageResource(R.drawable.toggle_down)
                    else if (newState == BottomSheetBehavior.STATE_COLLAPSED)
                        setImageResource(R.drawable.toggle_up)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                return
            }
        })
        // 메시지큐에 넣어서 실행 -> 뷰가 그려진 다음 위치를 알아내고 바텀 시트가 올라올 위치 계산
        // 이걸 lockerDetailImage.post로 바꿔야하지 않을까?
        binding.root.post {
            behavior.peekHeight = binding.root.measuredHeight - 320.dpToPx(requireContext())
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun blockBottomSheetTouchIntercept() {
        with(binding.bottomSheet) {
            recyclerview.setOnTouchListener { _, _ ->
                recyclerview.parent.requestDisallowInterceptTouchEvent(true)
                return@setOnTouchListener false
            }
        }
    }

    override fun observeData(): Job {
        Log.i("LockerDetailFragment", "observeData: ")
        val job = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.stateFlow.collect { state ->
                        when (state) {
                            is LockerDetailState.Uninitialized -> handleUninitialized(state)
                            is LockerDetailState.Loading -> Unit
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

    private fun handleUninitialized(lockerDetailState: LockerDetailState.Uninitialized) {
        requireActivity().showShortToast(lockerDetailState.lockerId.toString())
    }

    //    private fun handleLoading(lockerListState: LockerListState) {
//
//    }
//
    private fun handleSuccess(lockerDetailState: LockerDetailState.Success) {
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

        fun newInstance() = LockerDetailFragment()
    }
}
