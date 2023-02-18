package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddItemMarkerMap
import com.yapp.itemfinder.domain.model.Item
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.feature.common.databinding.ViewholderAddItemMarkerMapBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddItemMarkerMapViewHolder(
    val binding: ViewholderAddItemMarkerMapBinding
): DataViewHolder<AddItemMarkerMap>(binding) {

    override fun reset() = Unit

    override fun bindViews(data: AddItemMarkerMap) {
        binding.root.setOnClickListener {
            data.runMoveItemPositionDefine()
        }
    }

    override fun bindData(data: AddItemMarkerMap) {
        super.bindData(data)
        data.itemCategorySetHandler = {
            val item = data.item?.copy(itemCategory = it)
            setItem(data.lockerEntity, item)
        }

        val item = data.item
        setItem(data.lockerEntity, item)
    }

    private fun setItem(lockerEntity: LockerEntity, item: Item?) = with(binding) {
        itemsMarkerMapView.fetchItems(
            lockerEntity = lockerEntity,
            items = if (item?.position != null) listOf(item) else listOf()
        )
        item?.let { itemsMarkerMapView.applyFocusMarker(it) }
        lockerEntity.imageUrl?.let { itemsMarkerMapView.setBackgroundImage(it) }
    }

}
