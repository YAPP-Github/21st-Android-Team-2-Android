package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import com.yapp.itemfinder.domain.model.AddItemMarkerMap
import com.yapp.itemfinder.feature.common.databinding.ViewholderAddItemMarkerMapBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddItemMarkerMapViewHolder(
    val binding: ViewholderAddItemMarkerMapBinding
): DataViewHolder<AddItemMarkerMap>(binding) {

    override fun reset() = Unit

    override fun bindViews(data: AddItemMarkerMap) {
        with(binding) {
            val item = data.item
            itemsMarkerMapView.fetchItems(
                lockerEntity = data.lockerEntity,
                items = if (item != null) listOf(item) else listOf()
            )
            item?.let { itemsMarkerMapView.applyFocusMarker(it) }
        }
    }
}
