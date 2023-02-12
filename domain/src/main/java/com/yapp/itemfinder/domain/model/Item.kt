package com.yapp.itemfinder.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yapp.itemfinder.domain.R

typealias ItemFocusHandler = (Boolean) -> Unit

data class Item(
    override var id: Long,
    var lockerId: Long? = null,
    var name: String,
    var expirationDate: String? = null,
    var purchaseDate: String? =null,
    var memo: String? = null,
    var imageUrls: List<String>? = null,
    val itemCategory: ItemCategory? = null,
    var tags: List<Tag>? = null,
    val count: Int = 0,
    override var type: CellType = CellType.ITEM_SIMPLE_CELL,
    val position: Position? = null,
) : Data(type = CellType.ITEM_SIMPLE_CELL) {

    val representativeImage: String?
            get() = imageUrls?.first()

    data class Position(
        @androidx.annotation.FloatRange(from = 0.0, to = 100.0) val x: Float,
        @androidx.annotation.FloatRange(from = 0.0, to = 100.0) val y: Float
    )

    var itemFocusHandler: ItemFocusHandler = { }

    fun applyItemFocus(isFocused: Boolean) = itemFocusHandler(isFocused)

}

enum class ItemCategory(
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int
) {
    LIVING(R.string.item_category_living, R.drawable.ic_marker_living),
    FOOD(R.string.item_category_food, R.drawable.ic_marker_food),
    FASHION(R.string.item_category_fashion, R.drawable.ic_marker_fashion);
}
