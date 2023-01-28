package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.AddItemImages
import com.yapp.itemfinder.feature.common.adaper.AddItemImagesInnerAdapter
import com.yapp.itemfinder.feature.common.databinding.AddItemImagesBinding
import com.yapp.itemfinder.feature.common.utility.LastItemRightMarginZeroDecoration

class AddItemImagesViewHolder(
    val binding: AddItemImagesBinding
) : DataViewHolder<AddItemImages>(binding) {
    lateinit var mAdapter: AddItemImagesInnerAdapter

    override fun reset() {
        return
    }

    override fun bindData(data: AddItemImages) {
        super.bindData(data)

        binding.innerRecyclerView.apply {
            if (adapter == null) {
                mAdapter = AddItemImagesInnerAdapter { data.addCameraAction() }
                adapter = mAdapter
            }
        }
        mAdapter.submitList(
            listOf(
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "123",
                "12324",
                "13213",
                "122324",
                "131232143213"
            )
        )
    }

    override fun bindViews(data: AddItemImages) {
        binding.innerRecyclerView.addItemDecoration(LastItemRightMarginZeroDecoration())
    }
}
