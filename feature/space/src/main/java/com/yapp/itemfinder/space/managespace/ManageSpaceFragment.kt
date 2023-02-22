package com.yapp.itemfinder.space.managespace

import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.space.databinding.FragmentManageSpaceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ManageSpaceFragment : BaseStateFragment<ManageSpaceViewModel, FragmentManageSpaceBinding>() {

    override val vm by viewModels<ManageSpaceViewModel>()

    override val binding by viewBinding(FragmentManageSpaceBinding::inflate)

    override val depth: Depth
        get() = Depth.SECOND

    private var dataListAdapter: DataListAdapter<Data>? = null

    private val mySpaceTitle by lazy { requireArguments().getString(MY_SPACE_TITLE_KEY) }

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        initToolBar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
        }
        recyclerView.itemAnimator = null
        recyclerView.adapter = dataListAdapter
        setFragmentResultListener(AddSpaceDialog.NEW_SPACE_REQUEST_KEY) { requestKey, bundle ->
            val newSpaceName = bundle.getString(AddSpaceDialog.NEW_SPACE_NAME_BUNDLE_KEY)
            if (newSpaceName != null) {
                vm.addSpace(newSpaceName)
            }
        }
        setFragmentResultListener(EditSpaceDialog.EDIT_NAME_REQUEST_KEY) { requestKey, bundle ->
            val newSpaceName = bundle.getString(EditSpaceDialog.NEW_SPACE_NAME_KEY)
            val spaceId = bundle.getLong(EditSpaceDialog.SPACE_ID_KEY)
            if (newSpaceName != null) {
                vm.editSpace(spaceId, newSpaceName)
            }
        }
        setFragmentResultListener(DeleteSpaceDialog.DELETE_SPACE_REQUEST) { _, bundle ->
            val spaceId = bundle.getLong(DeleteSpaceDialog.SPACE_ID)
            vm.deleteSpace(spaceId)
        }
    }

    private fun initToolBar() = with(binding.defaultTopNavigationView) {
        backButtonImageResId = CR.drawable.ic_back
        backButtonClickListener = { backPressed() }

        containerColor = CR.color.brown_02
        titleText = mySpaceTitle

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
                            is ManageSpaceState.Uninitialized -> Unit
                            is ManageSpaceState.Loading -> handleLoading(state)
                            is ManageSpaceState.Success -> handleSuccess(state)
                            is ManageSpaceState.Error -> handleError(state)
                        }
                    }
                }
                launch {
                    vm.sideEffectFlow.collect { sideEffect ->
                        when (sideEffect) {
                            is ManageSpaceSideEffect.OpenAddSpaceDialog -> {
                                val dialog: AddSpaceDialog = AddSpaceDialog.newInstance()
                                activity?.supportFragmentManager?.let { fragmentManager ->
                                    dialog.show(fragmentManager, AddSpaceDialog.TAG)
                                }
                            }
                            is ManageSpaceSideEffect.OpenEditSpaceDialog -> {
                                val dialog: EditSpaceDialog = EditSpaceDialog.newInstance()
                                val bundle = Bundle().apply {
                                    putLong(EditSpaceDialog.SPACE_ID, sideEffect.space.id)
                                    putString(EditSpaceDialog.SPACE_NAME, sideEffect.space.name)
                                }
                                dialog.arguments = bundle
                                activity?.supportFragmentManager?.let { fragmentManager ->
                                    dialog.show(fragmentManager, EditSpaceDialog.TAG)
                                }
                            }
                            is ManageSpaceSideEffect.ShowToast -> {
                                Toast.makeText(
                                    requireContext(),
                                    sideEffect.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is ManageSpaceSideEffect.OpenDeleteSpaceDialog -> {
                                val dialog = DeleteSpaceDialog.newInstance()
                                dialog.arguments = Bundle().apply {
                                    putLong(DeleteSpaceDialog.SPACE_ID, sideEffect.spaceId)
                                    putString(DeleteSpaceDialog.SPACE_NAME, sideEffect.spaceName)
                                }
                                activity?.supportFragmentManager?.let { fm ->
                                    dialog.show(fm, DeleteSpaceDialog.TAG)
                                }
                            }
                            is ManageSpaceSideEffect.AddSpaceSuccessResult -> {
                                setFragmentResult(NEW_SPACE_ADDED_REQUEST_KEY, bundleOf())
                            }
                        }
                    }
                }
            }
        }
        return job
    }

    private fun handleLoading(manageSpaceState: ManageSpaceState) {

    }

    private fun handleSuccess(manageSpaceState: ManageSpaceState.Success) {
        dataBindHelper.bindList(manageSpaceState.dataList, vm)
        dataListAdapter?.submitList(manageSpaceState.dataList)
    }

    private fun handleError(manageSpaceState: ManageSpaceState.Error) {

    }

    companion object {

        val TAG = ManageSpaceFragment::class.simpleName.toString()
        const val MY_SPACE_TITLE_KEY = "MY_SPACE_TITLE_KEY"
        const val NEW_SPACE_ADDED_REQUEST_KEY = "NEW_SPACE_ADDED"

        fun newInstance() = ManageSpaceFragment()

    }

}
