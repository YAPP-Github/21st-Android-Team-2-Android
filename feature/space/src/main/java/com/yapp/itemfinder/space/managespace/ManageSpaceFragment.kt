package com.yapp.itemfinder.space.managespace

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.databinding.FragmentManageSpaceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ManageSpaceFragment : BaseStateFragment<ManageSpaceViewModel, FragmentManageSpaceBinding>() {

    override val vm by viewModels<ManageSpaceViewModel>()

    override val binding by viewBinding(FragmentManageSpaceBinding::inflate)

    private var dataListAdapter: DataListAdapter<Data>? = null

    private val mySpaceTitle by lazy { requireArguments().getString(MY_SPACE_TITLE_KEY) }

    @Inject
    lateinit var dataBindHelper: DataBindHelper

    override fun initViews() = with(binding) {
        initToolBar()
        if (dataListAdapter == null) {
            dataListAdapter = DataListAdapter()
            recyclerView.adapter = dataListAdapter
        }
        setFragmentResultListener(NEW_SPACE_NAME_REQUEST_KEY) { requestKey, bundle ->
            val newSpaceName = bundle.getString(NAME_KEY)
            if (newSpaceName != null) {
                vm.addItem(newSpaceName)
            }
        }
    }

    private fun initToolBar() = with(binding) {
        defaultTopNavigationView.backButtonImageResId = com.yapp.itemfinder.feature.common.R.drawable.ic_back
        defaultTopNavigationView.backButtonClickListener = {
            onBackPressedCallback.handleOnBackPressed()
        }

        defaultTopNavigationView.containerColor = com.yapp.itemfinder.feature.common.R.color.brown_02
        defaultTopNavigationView.titleText = mySpaceTitle

        defaultTopNavigationView.rightFirstIcon = com.yapp.itemfinder.feature.common.R.drawable.ic_reorder
        defaultTopNavigationView.rightFirstIconClickListener = {
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
                                val dialog: AddSpaceDialog = AddSpaceDialog().getInstance()
                                activity?.supportFragmentManager?.let { fragmentManager ->
                                    dialog.show(
                                        fragmentManager, ADD_SPACE_DIALOG_TAG
                                    )
                                }
                            }
                            is ManageSpaceSideEffect.AddSpaceFailedToast -> {
                                Toast.makeText(requireContext(), getString(R.string.failedToAddSpace), Toast.LENGTH_SHORT )
                            }
                            is ManageSpaceSideEffect.DeleteDialog -> {
                                activity?.let {
                                    val builder = AlertDialog.Builder(
                                        ContextThemeWrapper(
                                            it,
                                            com.yapp.itemfinder.feature.common.R.style.AlertDialog
                                        )
                                    )
                                    builder.setMessage("공간 삭제")
                                        .setPositiveButton("삭제",
                                            DialogInterface.OnClickListener { dialog, id ->
                                                Toast.makeText(context, "삭제", Toast.LENGTH_SHORT)
                                                    .show()
                                            })
                                        .setNegativeButton("취소",
                                            DialogInterface.OnClickListener { dialog, id ->
                                                // User cancelled the dialog
                                            })
                                    val dialog = builder.create()
                                    dialog.show()
                                }
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

        const val NEW_SPACE_NAME_REQUEST_KEY = "new space name"
        const val ADD_SPACE_DIALOG_TAG = "add space dialog"
        const val NAME_KEY = "name"
        const val MY_SPACE_TITLE_KEY = "MY_SPACE_TITLE_KEY"

        fun newInstance() = ManageSpaceFragment()

    }

}
