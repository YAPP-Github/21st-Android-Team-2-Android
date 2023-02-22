package com.yapp.itemfinder.space

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.domain.model.ScreenMode
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.feature.common.FragmentNavigator
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.space.additem.AddItemActivity
import com.yapp.itemfinder.space.addlocker.AddLockerActivity
import com.yapp.itemfinder.space.databinding.FragmentLockerListBinding
import com.yapp.itemfinder.space.lockerdetail.LockerDetailFragment
import com.yapp.itemfinder.space.lockerlist.DeleteLockerDialog
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

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun initViews() = with(binding) {
        initToolBar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
        }
        recyclerView.adapter = dataListAdapter
        recyclerView.itemAnimator = null
        addItemFAB.setOnClickListener { vm.moveAddItemActivity() }
        setResultLauncher()
        setFragmentResultListener(DeleteLockerDialog.CONFIRM_KEY) { _, bundle ->
            val lockerId = bundle.getLong(DeleteLockerDialog.DELETE_LOCKER_ID)
            vm.deleteLocker(lockerId)
        }
    }

    private fun initToolBar() = with(binding.defaultTopNavigationView) {
        backButtonImageResId = CR.drawable.ic_back
        backButtonClickListener = { backPressed() }

        containerColor = CR.color.brown_02
        titleText = requireArguments().getString(SPACE_NAME_KEY)

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
                                intent.apply {
                                    putExtra(
                                        AddLockerActivity.SPACE_ID_KEY,
                                        requireArguments().getLong(SPACE_ID_KEY)
                                    )
                                    putExtra(
                                        AddLockerActivity.SPACE_NAME_KEY,
                                        requireArguments().getString(SPACE_NAME_KEY)
                                    )
                                    putExtra(
                                        AddLockerActivity.SCREEN_MODE,
                                        ScreenMode.ADD_MODE.label
                                    )
                                }
                                resultLauncher.launch(intent)
                            }
                            is LockerListSideEffect.MoveToAddItem -> {
                                startActivity(AddItemActivity.newIntent(requireContext()).apply {
                                    putExtra(AddItemActivity.SCREEN_MODE, ScreenMode.ADD_MODE.label)
                                })
                            }
                            is LockerListSideEffect.MoveToEditLocker -> {
                                val intent = AddLockerActivity.newIntent(requireActivity())
                                intent.apply {
                                    putExtra(
                                        AddLockerActivity.SPACE_ID_KEY,
                                        requireArguments().getLong(SPACE_ID_KEY)
                                    )
                                    putExtra(
                                        AddLockerActivity.SPACE_NAME_KEY,
                                        requireArguments().getString(SPACE_NAME_KEY)
                                    )
                                    putExtra(
                                        AddLockerActivity.SCREEN_MODE,
                                        ScreenMode.EDIT_MODE.label
                                    )
                                    putExtra(
                                        AddLockerActivity.LOCKER_ENTITY_KEY,
                                        sideEffect.locker
                                    )
                                }
                                resultLauncher.launch(intent)
                            }
                            is LockerListSideEffect.OpenDeleteLockerDialog -> {
                                val dialog = DeleteLockerDialog.newInstance()
                                dialog.arguments = Bundle().apply {
                                    putString(
                                        DeleteLockerDialog.DELETE_LOCKER_NAME,
                                        sideEffect.lockerName
                                    )
                                    putLong(
                                        DeleteLockerDialog.DELETE_LOCKER_ID,
                                        sideEffect.lockerId
                                    )
                                }
                                requireActivity().supportFragmentManager.let { fm ->
                                    dialog.show(fm, DELETE_LOCKER_DIALOG)
                                }
                            }
                            is LockerListSideEffect.ShowToast -> {
                                Toast.makeText(
                                    requireContext(),
                                    sideEffect.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
                    vm.fetchData()
                }
            }
    }

    companion object {

        val TAG = LockerListFragment::class.simpleName.toString()
        const val SPACE_ID_REQUEST_KEY = "space id for locker list screen"
        const val SPACE_ID_KEY = "spaceId"
        const val SPACE_NAME_KEY = "SPACE_NAME_KEY"
        const val SPACE_ITEM_KEY = "SPACE_ITEM_KEY"
        const val DELETE_LOCKER_DIALOG = "DELETE_LOCKER_DIALOG"

        fun newInstance() = LockerListFragment()

    }
}
