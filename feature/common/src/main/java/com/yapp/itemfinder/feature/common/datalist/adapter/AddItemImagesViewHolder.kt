package com.yapp.itemfinder.feature.common.datalist.adapter

import com.yapp.itemfinder.domain.model.AddItemImages
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.adaper.AddItemImagesInnerAdapter
import com.yapp.itemfinder.feature.common.adaper.AddItemImagesInnerData
import com.yapp.itemfinder.feature.common.databinding.AddItemImagesBinding
import com.yapp.itemfinder.feature.common.utility.AddImagesItemDecoration

class AddItemImagesViewHolder(
    val binding: AddItemImagesBinding
) : DataViewHolder<AddItemImages>(binding) {
    lateinit var mAdapter: AddItemImagesInnerAdapter

    private var isInnerRcvInitiated = false
    private val context = binding.root.context
    private val addImagesDecoration by lazy {
        AddImagesItemDecoration(context.resources.getDimension(R.dimen.add_images_recyclerview_margin).toInt())
    }

    override fun reset() {
        return
    }

    override fun bindData(data: AddItemImages) {
        super.bindData(data)

        if (isInnerRcvInitiated.not()){
            isInnerRcvInitiated = true
            with(binding.innerRecyclerView){
                mAdapter = AddItemImagesInnerAdapter { data.addCameraAction() }
                adapter = mAdapter
                addItemDecoration(addImagesDecoration)
            }
        }
        val cameraData = AddItemImagesInnerData.AddItemImagesInnerCameraData(
            currentCount = data.uriStringList.size,
            maxCount =  data.maxCount
        )
        val dataList = listOf(cameraData) + data.uriStringList.map {
            AddItemImagesInnerData.AddItemImagesInnerImageData(it)
        }
        mAdapter.submitList(dataList)
    }

    override fun bindViews(data: AddItemImages) {

    }
}
