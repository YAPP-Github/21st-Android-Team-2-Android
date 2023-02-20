package com.yapp.itemfinder.space.itemdetail

import android.app.Activity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.setFragmentResult
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
    }

    private fun initToolBar() = with(binding.defaultTopNavigationView) {
        backButtonImageResId = CR.drawable.ic_back_white
        backButtonClickListener = {
            onBackPressedCallback.handleOnBackPressed()
        }
        containerColor = depth.colorId

        rightFirstIcon = CR.drawable.ic_delete_white
        rightFirstIconClickListener = {
            Toast.makeText(requireContext(), "삭제 버튼 클릭", Toast.LENGTH_SHORT).show()
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
                                    putExtra(AddItemActivity.ITEM_ID_KEY, requireArguments().getLong(ITEM_ID_KEY))
                                    putExtra(AddItemActivity.SCREEN_MODE, ScreenMode.EDIT_MODE.label)
                                    putExtra(AddItemActivity.SELECTED_SPACE_AND_LOCKER_KEY, sideEffect.spaceAndLockerEntity)
                                }
                                editItemLauncher.launch(intent)
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
        if (item.imageUrls.isNullOrEmpty()) {
            itemMainImage.gone()
            itemImagesLayout.gone()
            binding.itemMarginView.visible()
            with(binding.defaultTopNavigationView) {
                containerColor = depth.colorId
                backButtonImageResId = CR.drawable.ic_back
                rightFirstIcon = CR.drawable.ic_delete
                rightSecondIcon = CR.drawable.ic_edit
            }
        }else{
            with(binding.defaultTopNavigationView) {
                containerColor = CR.color.transparent
                backButtonImageResId = CR.drawable.ic_back_white
                rightFirstIcon = CR.drawable.ic_delete_white
                rightSecondIcon = CR.drawable.ic_edit_white
            }
            itemMainImage.visible()
            binding.itemMarginView.gone()
            Glide.with(requireContext()).load(item.representativeImage).into(itemMainImage)

            if (item.otherImages.isNullOrEmpty()){
                itemImagesLayout.gone()
            }else{
                imageRecyclerView.adapter = itemImageAdapter
                itemImageAdapter.submitList(item.otherImages)
              // 이미지 넣기
            }

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
                val chipDrawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, com.yapp.itemfinder.feature.common.R.style.TagChip)
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
            if (it.isNotEmpty()){
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
        if (isEditSucceed) {
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
