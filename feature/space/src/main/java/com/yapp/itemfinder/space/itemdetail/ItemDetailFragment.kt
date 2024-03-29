package com.yapp.itemfinder.space.itemdetail

import android.app.Activity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.yapp.itemfinder.domain.model.ScreenMode
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.adaper.ItemImageAdapter
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.extension.dpToPx
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.visible
import com.yapp.itemfinder.feature.common.utility.ImagesItemDecoration
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.space.additem.AddItemActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.yapp.itemfinder.space.databinding.FragmentItemDetailBinding
import com.yapp.itemfinder.space.lockerdetail.LockerDetailFragment

@AndroidEntryPoint
class ItemDetailFragment : BaseStateFragment<ItemDetailViewModel, FragmentItemDetailBinding>() {

    override val vm by viewModels<ItemDetailViewModel>()
    private val itemImageAdapter by lazy { ItemImageAdapter() }
    private val imageItemDecoration by lazy { ImagesItemDecoration(4.dpToPx(requireContext())) }

    override val depth: Depth
        get() = Depth.SECOND

    override val binding by viewBinding(FragmentItemDetailBinding::inflate)

    private var isEditSucceed: Boolean = false
    private var isDeleteSucceed: Boolean = false

    private val editItemLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                vm.reFetchData()
                isEditSucceed = true
            }
        }

    override fun initViews() = with(binding) {
        initToolBar()
        imageRecyclerView.addItemDecoration(imageItemDecoration)
        setFragmentResultListener(DeleteItemDialog.DELETE_ITEM_REQUEST) { _, _ ->
            vm.deleteItem()
            isDeleteSucceed = true
        }
    }

    private fun initToolBar() = with(binding.defaultTopNavigationView) {
        backButtonImageResId = CR.drawable.ic_back_white
        backButtonClickListener = { backPressed() }
        containerColor = depth.colorId

        rightFirstIcon = CR.drawable.ic_delete_white
        rightFirstIconClickListener = {
            vm.openDeleteDialog()
        }
        rightSecondIcon = CR.drawable.ic_edit_white
        rightSecondIconClickListener = {
            vm.moveToEdit()
        }
    }


    override fun observeData(): Job {
        val job = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.stateFlow.collect { state ->
                        when (state) {
                            is ItemDetailState.Uninitialized -> Unit
                            is ItemDetailState.Loading -> handleLoading()
                            is ItemDetailState.Success -> handleSuccess(state)
                            is ItemDetailState.Error -> Unit
                        }
                    }
                }
                launch {
                    vm.sideEffectFlow.collect { sideEffect ->
                        when (sideEffect) {
                            is ItemDetailSideEffect.MoveToEditItem -> {
                                val intent = AddItemActivity.newIntent(requireContext()).apply {
                                    putExtra(
                                        AddItemActivity.ITEM_ID_KEY,
                                        requireArguments().getLong(ITEM_ID_KEY)
                                    )
                                    putExtra(
                                        AddItemActivity.SCREEN_MODE,
                                        ScreenMode.EDIT_MODE.label
                                    )
                                    putExtra(
                                        AddItemActivity.SELECTED_SPACE_AND_LOCKER_KEY,
                                        sideEffect.spaceAndLockerEntity
                                    )
                                }
                                editItemLauncher.launch(intent)
                            }
                            is ItemDetailSideEffect.OpenDeleteDialog -> {
                                val dialog = DeleteItemDialog.newInstance()
                                activity?.supportFragmentManager?.let { fm ->
                                    dialog.show(fm, DeleteItemDialog.TAG)
                                }
                            }
                            is ItemDetailSideEffect.ShowToast -> {
                                Toast.makeText(
                                    requireContext(),
                                    sideEffect.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is ItemDetailSideEffect.Finish -> {
                                val fm = requireActivity().supportFragmentManager
                                fm.beginTransaction().remove(this@ItemDetailFragment).commit()
                                fm.popBackStack()
                            }
                        }
                    }
                }
            }
        }
        return job
    }

    private fun handleLoading() {

    }

    private fun handleSuccess(lockerDetailState: ItemDetailState.Success) = with(binding) {
        val item = lockerDetailState.item
        // 메인 이미지 관련 뷰 초기화
        if (item.imageUrls.isNullOrEmpty()) {
            itemMainImage.gone()
            binding.itemMarginView.visible()
            with(binding.defaultTopNavigationView) {
                containerColor = depth.colorId
                backButtonImageResId = CR.drawable.ic_back
                rightFirstIcon = CR.drawable.ic_delete
                rightSecondIcon = CR.drawable.ic_edit
            }
        } else {
            with(binding.defaultTopNavigationView) {
                containerColor = CR.color.transparent
                backButtonImageResId = CR.drawable.ic_back_white
                rightFirstIcon = CR.drawable.ic_delete_white
                rightSecondIcon = CR.drawable.ic_edit_white
            }
            itemMainImage.visible()
            binding.itemMarginView.gone()
            Glide.with(requireContext()).load(item.representativeImage).into(itemMainImage)

        }
        // 나머지 이미지 관련 초기화
        if (item.otherImages.isNullOrEmpty()){
            itemImagesLayout.gone()
        }else{
            itemImagesLayout.visible()
            imageRecyclerView.adapter = itemImageAdapter
            itemImageAdapter.submitList(item.otherImages)
        }
        itemName.text = item.name
        itemCategory.text = item.itemCategory?.labelResId?.let { getString(it) }
        lockerDetailState.spaceAndLockerEntity?.let { (space, locker) ->
            itemSpace.text = space.name
            itemLocker.text = locker?.name
        }
        itemCount.text = getString(CR.string.count, item.count)
        item.tags?.let { tags ->
            tags.asReversed().forEach {
                val tagChip = Chip(context)
                val chipDrawable = ChipDrawable.createFromAttributes(
                    requireContext(),
                    null,
                    0,
                    com.yapp.itemfinder.feature.common.R.style.TagChip
                )
                binding.tagChipGroup.addView(
                    tagChip.apply {
                        id = ViewCompat.generateViewId()
                        setChipDrawable(chipDrawable)
                        setTextColor(ContextCompat.getColor(context, CR.color.gray_05))
                        setEnsureMinTouchTargetSize(false)
                        text = it.name
                    }
                )
            }
        }

        item.memo?.let {
            if (it.isNotEmpty()) {
                itemMemo.visible()
                itemMemoTitle.visible()
                itemMemo.text = it
            }
        }
        item.expirationDate?.let {
            itemExpirationDateTitle.visible()
            itemExpirationDate.visible()
            itemExpirationDate.text = it
        }
        item.purchaseDate?.let {
            itemPurchaseDate.visible()
            itemPurchaseDateTitle.visible()
            itemPurchaseDate.text = it
        }
        item.position?.let {
            lockerMarkerMap.visible()
            lockerMarkerMap.addMarkerAndBringToFront(item)
        }

        item.containerImageUrl?.let { imageUrl ->
            lockerMarkerMap.setBackgroundImage(imageUrl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isEditSucceed || isDeleteSucceed) {
            setFragmentResult(
                LockerDetailFragment.FETCH_REQUEST_KEY,
                bundleOf(
                    LockerDetailFragment.FETCH_RESULT_KEY to true
                )
            )
        }
    }

    companion object {

        val TAG = ItemDetailFragment::class.simpleName.toString()

        const val ITEM_ID_KEY = "ITEM_ID_KEY"
        const val SPACE_AND_LOCKER_KEY = "SPACE_AND_LOCKER_KEY"

        fun newInstance() = ItemDetailFragment()
    }
}
