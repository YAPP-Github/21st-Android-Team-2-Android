package com.yapp.itemfinder.space.itemdetail

import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.yapp.itemfinder.domain.model.ScreenMode
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.extension.visible
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.additem.AddItemActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.yapp.itemfinder.feature.common.R as CR
import com.yapp.itemfinder.space.databinding.FragmentItemDetailBinding

@AndroidEntryPoint
class ItemDetailFragment : BaseStateFragment<ItemDetailViewModel, FragmentItemDetailBinding>() {

    override val vm by viewModels<ItemDetailViewModel>()

    override val depth: Depth
        get() = Depth.SECOND

    override val binding by viewBinding(FragmentItemDetailBinding::inflate)

    override fun initViews() = with(binding) {
        initToolBar()
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
            val intent = AddItemActivity.newIntent(requireContext()).apply {
                putExtra(ITEM_ID_KEY, requireArguments().getLong(ITEM_ID_KEY))
                putExtra(AddItemActivity.SCREEN_MODE, ScreenMode.EDIT_MODE.label)
            }
            startActivity(intent)
        }
        containerColor = CR.color.transparent
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
                                // EditItemActivity
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
        if (item.representativeImage != null) {
            Glide.with(requireContext()).load(item.representativeImage).into(itemMainImage)
        }
        itemName.text = item.name
        itemCategory.text = item.itemCategory?.labelResId?.let { getString(it) }
        itemSpace.text = arguments?.getString(SPACE_NAME_KEY)
        itemLocker.text = arguments?.getString(LOCKER_NAME_KEY)
        itemCount.text = getString(R.string.countText, item.count)
        item.tags?.let {
            itemTagsLayout.visible()
            itemTagTitle.visible()
            it.forEach {
                val tv = TextView(context)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, 0, 8, 0)
                tv.apply {
                    text = it.name
                    setTextColor(
                        ResourcesCompat.getColor(
                            requireActivity().resources, CR.color.gray_05, null
                        )
                    )
                    background = ResourcesCompat.getDrawable(
                        requireActivity().resources,
                        CR.drawable.bg_chip_tag,
                        null
                    )
                    setTextAppearance(CR.style.ItemFinderTypography)
                    layoutParams = params
                }
                itemTagsLayout.addView(tv)
            }
        }

        item.memo?.let {
            itemMemo.visible()
            itemMemoTitle.visible()
            itemMemo.text = it
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
    }

    companion object {

        val TAG = ItemDetailFragment::class.simpleName.toString()

        const val ITEM_ID_KEY = "ITEM_ID_KEY"
        const val SPACE_NAME_KEY = "SPACE_NAME_KEY"
        const val LOCKER_NAME_KEY = "LOCKER_NAME_KEY"

        fun newInstance() = ItemDetailFragment()
    }
}
