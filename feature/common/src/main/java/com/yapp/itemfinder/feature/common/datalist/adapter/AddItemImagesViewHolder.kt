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


    private var prevData: AddItemImages? = null
    private var dataChanged = false
    private val context = binding.root.context
    private val addImagesDecoration by lazy {
        AddImagesItemDecoration(context.resources.getDimension(R.dimen.add_images_recyclerview_margin).toInt())
    }
    init {
        binding.innerRecyclerView.addItemDecoration(addImagesDecoration)
    }

    override fun reset() {
        return
    }

    override fun bindData(data: AddItemImages) {
        super.bindData(data)

        val cameraData = AddItemImagesInnerData.AddItemImagesInnerCameraData(
            currentCount = data.uriStringList.size,
            maxCount =  data.maxCount
        )
        val dataList = listOf(cameraData) + data.uriStringList.map {
            AddItemImagesInnerData.AddItemImagesInnerImageData(it)
        }
        if (prevData == null){
            prevData = data
            dataChanged = true
        }
        else if (prevData != data){
            dataChanged = true
            prevData = data
        }

        // 어댑터가 매번 생성되지 않도록 변경합니다. AddItemImage가 변경된 경우 새로 생성합니다.
        if (dataChanged){
            dataChanged = false

            with(binding.innerRecyclerView){
                mAdapter = AddItemImagesInnerAdapter(
                    onCameraClicked = data.addCameraActionHandler,
                    onCancelClicked = { position ->
                        val  newUriStringList = data.uriStringList.toMutableList().apply { removeAt(position-1) }
                        data.cancelImageUploadHandler(newUriStringList)
                    }
                )
                adapter = mAdapter
            }
        }
        mAdapter.submitList(dataList)
    }

    override fun bindViews(data: AddItemImages) {

    }
}
