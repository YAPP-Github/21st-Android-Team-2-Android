package com.yapp.itemfinder.space.additem.itemposition

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.yapp.itemfinder.domain.model.LockerAndItemEntity
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.databinding.ActivityAddItemPositionDefineBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddItemPositionDefineActivity : BaseStateActivity<AddItemPositionDefineViewModel, ActivityAddItemPositionDefineBinding>() {

    override val depth: Depth
        get() = Depth.SECOND

    override val vm by viewModels<AddItemPositionDefineViewModel>()

    override val binding by viewBinding(ActivityAddItemPositionDefineBinding::inflate)

    override fun initViews() = with(binding) {
        initToolbar()

        initializePinButton.setOnClickListener {
            vm.initializePin()
        }
    }

    private fun initToolbar() = with(binding.defaultNavigationView) {
        backButtonImageResId = CR.drawable.ic_back
        containerColor = CR.color.brown_02
        backButtonClickListener = { finish() }
        titleText = getString(R.string.item_position)
        rightFirstIcon = CR.drawable.ic_done
        rightFirstIconClickListener = {
            vm.saveItemPosition()
        }
    }

    override fun observeData(): Job = lifecycleScope.launch {
        launch {
            vm.stateFlow.collect { state ->
                when (state) {
                    is AddItemPositionDefineState.Uninitialized -> Unit
                    is AddItemPositionDefineState.Loading -> handleLoading(state)
                    is AddItemPositionDefineState.Success -> handleSuccess(state)
                    is AddItemPositionDefineState.Error -> handleError(state)
                }
            }
        }
        launch {
            vm.sideEffectFlow.collect { sideEffect ->

            }
        }

    }

    private fun handleLoading(state: AddItemPositionDefineState) {

    }

    private fun handleSuccess(state: AddItemPositionDefineState.Success) = with(binding) {
        val (locker, item) = state.lockerAndItemEntity

        itemsMarkerMapView.fetchItems(locker, if (item == null) listOf() else listOf(item))
        item?.let { itemsMarkerMapView.applyFocusMarker(it) }

        val isItemExist = item != null
        defineItemPositionTextView.isVisible = isItemExist.not()
        initializePinButton.isVisible = isItemExist
    }

    private fun handleError(state: AddItemPositionDefineState) {

    }

    companion object {

        const val LOCKER_AND_ITEM_KEY = "LOCKER_AND_ITEM_KEY"

        fun newIntent(context: Context, lockerAndItemEntity: LockerAndItemEntity) =
            Intent(context, AddItemPositionDefineActivity::class.java).apply {
                putExtra(LOCKER_AND_ITEM_KEY, lockerAndItemEntity)
            }

    }

}
