package com.yapp.itemfinder.space

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.FragmentNavigator
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.space.databinding.FragmentLockerListBinding
import com.yapp.itemfinder.space.lockerdetail.LockerDetailFragment
import com.yapp.itemfinder.space.managespace.ManageSpaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LockerListFragment : BaseStateFragment<LockerListViewModel, FragmentLockerListBinding>() {

    override val vm by viewModels<LockerListViewModel>()

    override val binding by viewBinding(FragmentLockerListBinding::inflate)

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
                            is LockerListState.Uninitialized -> Unit
                            is LockerListState.Loading -> handleLoading(state)
                            is LockerListState.Success -> handleSuccess(state)
                            is LockerListState.Error -> handleError(state)
                        }
                    }
                }
                launch {
                    vm.sideEffectFlow.collect { sideEffect ->
                        when (sideEffect) {
                            is LockerListSideEffect.MoveToLockerDetail -> {
                                // 이동
                                moveLockerDetail()

                            }
                            else -> {}
                        }
                    }
                }
            }
        }
        return job
    }

    private fun moveLockerDetail(){
        val activity = requireActivity()
        when (activity){
            is FragmentNavigator ->{
                activity.addFragmentBackStack(LockerDetailFragment.TAG)
            }
        }

    }

    private fun handleLoading(lockerListState: LockerListState) {

    }

    private fun handleSuccess(lockerListState: LockerListState.Success) {
        dataBindHelper.bindList(lockerListState.dataList, vm)
        dataListAdapter?.submitList(lockerListState.dataList)
    }

    private fun handleError(lockerListState: LockerListState.Error) {

    }

    companion object {

        val TAG = LockerListFragment::class.simpleName.toString()

        fun newInstance() = LockerListFragment()

    }
}
