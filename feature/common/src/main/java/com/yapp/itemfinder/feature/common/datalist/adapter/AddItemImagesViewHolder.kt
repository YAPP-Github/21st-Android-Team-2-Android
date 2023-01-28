package com.yapp.itemfinder.feature.common.datalist.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.yapp.itemfinder.domain.model.AddItemImages
import com.yapp.itemfinder.feature.common.adaper.AddItemImagesInnerAdapter
import com.yapp.itemfinder.feature.common.databinding.AddItemImagesBinding
import com.yapp.itemfinder.feature.common.utility.LastItemRightMarginZeroDecoration

class AddItemImagesViewHolder(
    val binding: AddItemImagesBinding
) : DataViewHolder<AddItemImages>(binding) {
    val context = binding.root.context

    val adapter = AddItemImagesInnerAdapter()

    override fun reset() {
        return
    }

    override fun bindData(data: AddItemImages) {
        super.bindData(data)
        binding.innerRecyclerView.adapter = adapter
        adapter.submitList(listOf("1", "2", "3", "4", "5", "6","123","12324","13213","122324","131232143213"))
    }

    override fun bindViews(data: AddItemImages) {
        binding.innerRecyclerView.addItemDecoration(LastItemRightMarginZeroDecoration())
    }
}
