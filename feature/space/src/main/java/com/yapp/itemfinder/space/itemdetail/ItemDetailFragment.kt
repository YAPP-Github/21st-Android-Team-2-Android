package com.yapp.itemfinder.space.itemdetail

import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.yapp.itemfinder.feature.common.BaseStateFragment
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.extension.visible
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
        backButtonImageResId = CR.drawable.ic_back
        backButtonClickListener = {
            onBackPressedCallback.handleOnBackPressed()
        }
        containerColor = depth.colorId

        rightFirstIcon = CR.drawable.ic_delete
        rightFirstIconClickListener = {
            Toast.makeText(requireContext(), "삭제 버튼 클릭", Toast.LENGTH_SHORT).show()
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
        itemName.text = item.name
        itemCategory.text = item.itemCategory?.label
        itemSpace.text = "주방"
        itemLocker.text = "냉장고"
        itemCount.text = "${item.count}개"
        if (item.tags?.isEmpty() == false) {
            itemTagsLayout.visible()
            itemTagTitle.visible()
            item.tags!!.forEach {
                // 커스텀뷰로
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
                            requireActivity().resources,
                            CR.color.gray_05,
                            null
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
        if (item.memo != null) {
            itemMemo.visible()
            itemMemoTitle.visible()
            itemMemo.text = item.memo
        }
        if (item.expirationDate != null) {
            itemExpirationDateTitle.visible()
            itemExpirationDate.visible()
            itemExpirationDate.text = item.expirationDate
        }
        if (item.purchaseDate != null) {
            itemPurchaseDate.visible()
            itemPurchaseDateTitle.visible()
            itemPurchaseDate.text = item.purchaseDate
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
