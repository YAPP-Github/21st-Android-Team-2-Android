package com.yapp.itemfinder.space

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.SpaceItem
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.feature.common.FragmentNavigator
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.extension.parcelable
import com.yapp.itemfinder.space.additem.AddItemActivity
import com.yapp.itemfinder.space.addlocker.AddLockerActivity
import com.yapp.itemfinder.space.databinding.FragmentLockerListBinding
import com.yapp.itemfinder.space.edititem.EditItemActivity
import com.yapp.itemfinder.space.lockerdetail.LockerDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LockerListFragment : BaseStateFragment<LockerListViewModel, FragmentLockerListBinding>() {

    override val vm by viewModels<LockerListViewModel>()

    override val binding by viewBinding(FragmentLockerListBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    override val depth: Depth
        get() = Depth.SECOND

    private val spaceItem by lazy { requireArguments().parcelable<SpaceItem>(SPACE_ITEM_KEY) }
    private val spaceId by lazy { requireArguments().getLong(SPACE_ID_KEY) }
    private val spaceName by lazy { requireArguments().getString(SPACE_NAME_KEY) }

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun initState() {
        super.initState()
        setFragmentResultListener(SPACE_ID_REQUEST_KEY) { requestKey, bundle ->
            val spaceId = bundle.getLong(SPACE_ID_KEY)
            vm.fetchLockerList(spaceId)
        }
    }

    override fun initViews() = with(binding) {
        initToolBar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
        }
        addItemFAB.setOnClickListener { vm.moveAddItemActivity() }
        setResultLauncher()
    }

    private fun initToolBar() = with(binding.defaultTopNavigationView) {
        backButtonImageResId = CR.drawable.ic_back
        backButtonClickListener = {
            onBackPressedCallback.handleOnBackPressed()
        }

        containerColor = CR.color.brown_02
        titleText = spaceName

        rightSecondIcon = CR.drawable.ic_search
        rightSecondIconClickListener = {
            Toast.makeText(requireContext(), "검색 버튼 클릭", Toast.LENGTH_SHORT).show()
        }

        rightFirstIcon = CR.drawable.ic_reorder
        rightFirstIconClickListener = {
            Toast.makeText(requireContext(), "정렬 버튼 클릭", Toast.LENGTH_SHORT).show()
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
                                moveLockerDetail(sideEffect.locker)
                            }
                            is LockerListSideEffect.MoveToAddLocker -> {
                                val intent = AddLockerActivity.newIntent(requireActivity())
                                spaceId.let { intent.putExtra(AddLockerActivity.SPACE_ID_KEY, it) }
                                spaceName.let {
                                    intent.putExtra(
                                        AddLockerActivity.SPACE_NAME_KEY,
                                        it
                                    )
                                }
                                resultLauncher.launch(intent)
                            }
                            is LockerListSideEffect.MoveToAddItem -> {
                                startActivity(AddItemActivity.newIntent(requireContext()))
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
        return job
    }

    private fun moveLockerDetail(locker: LockerEntity) {
        val activity = requireActivity()
        when (activity) {
            is FragmentNavigator -> {
                activity.addFragmentBackStack(
                    LockerDetailFragment.TAG,
                    bundle = bundleOf(LockerDetailFragment.LOCKER_ENTITY_KEY to locker)
                )
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

    private fun setResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    vm.fetchLockerList(spaceId)
                }
            }
    }

    companion object {

        val TAG = LockerListFragment::class.simpleName.toString()
        const val SPACE_ID_REQUEST_KEY = "space id for locker list screen"
        const val SPACE_ID_KEY = "spaceId"
        const val SPACE_NAME_KEY = "SPACE_NAME_KEY"
        const val SPACE_ITEM_KEY = "SPACE_ITEM_KEY"

        fun newInstance() = LockerListFragment()

    }
}
