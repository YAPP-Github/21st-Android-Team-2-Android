package com.yapp.itemfinder.space.managespace

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataListAdapter
import com.yapp.itemfinder.feature.common.datalist.binder.DataBindHelper
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
                            is ManageSpaceSideEffect.DeleteDialog -> {
                                activity?.let {
                                    val builder = AlertDialog.Builder(
                                        ContextThemeWrapper(
                                            it,
                                            com.yapp.itemfinder.feature.common.R.style.AlertDialog
                                        )
                                    )
                                    builder.setMessage("?????? ??????")
                                        .setPositiveButton("??????",
                                            DialogInterface.OnClickListener { dialog, id ->
                                                Toast.makeText(context, "??????", Toast.LENGTH_SHORT)
                                                    .show()
                                            })
                                        .setNegativeButton("??????",
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

        fun newInstance() = ManageSpaceFragment()

    }

}
