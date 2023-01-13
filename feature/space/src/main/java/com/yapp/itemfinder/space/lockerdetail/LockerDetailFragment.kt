package com.yapp.itemfinder.space.lockerdetail

import androidx.lifecycle.*
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.space.databinding.FragmentLockerDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LockerDetailFragment : BaseStateFragment<LockerDetailViewModel, FragmentLockerDetailBinding>() {

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
            recyclerView.adapter = dataListAdapter
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
//                        when (sideEffect) {
//                            is LockerListSideEffect.MoveToLockerDetail -> {
//                                // 이동
//                            }
//                            else -> {}
//                        }
                    }
                }
            }
        }
        return job
    }

    private fun handleUninitialized(lockerDetailState: LockerDetailState.Uninitialized){
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
