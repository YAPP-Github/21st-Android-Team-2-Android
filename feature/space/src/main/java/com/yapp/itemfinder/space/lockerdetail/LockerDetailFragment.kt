package com.yapp.itemfinder.space.lockerdetail

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.databinding.FragmentLockerDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LockerDetailFragment :
    BaseStateFragment<LockerDetailViewModel, FragmentLockerDetailBinding>() {

    override val vm by lazy {
        ViewModelProvider(
            this,
            LockerDetailViewModelFactory(requireArguments())
        )[LockerDetailViewModel::class.java]
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
                with(binding.bottomSheet.bottomSheetToggleImageView) {
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
        behavior.peekHeight = binding.lockerDetailImageView.height
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun blockBottomSheetTouchIntercept() {
        with(binding.bottomSheet) {
            rcvDouble.setOnTouchListener { _, _ ->
                rcvDouble.parent.requestDisallowInterceptTouchEvent(true)
                return@setOnTouchListener false
            }
        }
    }

    override fun observeData(): Job {
        val job = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.stateFlow.collect { state ->
                        when (state) {
                            is LockerDetailState.Uninitialized -> handleUninitialized(state)
                            is LockerDetailState.Loading -> Unit
                            is LockerDetailState.Success -> Unit
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
        requireActivity().showShortToast(lockerDetailState.locker.toString())
    }

//    private fun handleLoading(lockerListState: LockerListState) {
//
//    }
//
//    private fun handleSuccess(lockerListState: LockerListState.Success) {
//        dataBindHelper.bindList(lockerListState.dataList, vm)
//        dataListAdapter?.submitList(lockerListState.dataList)
//    }

//    private fun handleError(lockerListState: LockerListState.Error) {
//
//    }

    companion object {

        val TAG = LockerDetailFragment::class.simpleName.toString()

        fun newInstance() = LockerDetailFragment()
    }
}
