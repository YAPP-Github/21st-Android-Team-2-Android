package com.yapp.itemfinder.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yapp.itemfinder.domain.R

data class Item(
    override var id: Long,
    var lockerId: Long,
    var name: String,
    var expirationDate: String?,
    var imageUrl: String?,
    val itemCategory: ItemCategory?,
    var tags: List<Tag>?,
    val count: Int = 0,
    val position: Position? = null,
) : Data(type = CellType.ITEM_SIMPLE_CELL) {

    data class Position(
        @androidx.annotation.FloatRange(from = 0.0, to = 100.0) val x: Float,
        @androidx.annotation.FloatRange(from = 0.0, to = 100.0) val y: Float
    )

}

enum class ItemCategory(
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int
) {
    LIVING(R.string.item_category_living, R.drawable.ic_marker_living),
    FOOD(R.string.item_category_food, R.drawable.ic_marker_food),
    FASHION(R.string.item_category_fashion, R.drawable.ic_marker_fashion);
}
