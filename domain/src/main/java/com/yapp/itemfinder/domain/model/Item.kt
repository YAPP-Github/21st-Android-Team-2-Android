package com.yapp.itemfinder.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yapp.itemfinder.domain.R
import kotlinx.parcelize.Parcelize

typealias ItemFocusHandler = (Boolean) -> Unit

@Parcelize
data class Item(
    override var id: Long,
    var lockerId: Long,
    var name: String,
    var expirationDate: String?,
    var purchaseDate: String?,
    var memo: String?,
    var imageUrl: String?,
    val itemCategory: ItemCategory?,
    var tags: List<Tag>?,
    val count: Int = 0,
    override var type: CellType = CellType.ITEM_SIMPLE_CELL,
    val position: Position? = null,
) : Data(type = CellType.ITEM_SIMPLE_CELL), Parcelable {

    companion object {

        fun createEmptyItem() = Item(
            id = 0,
            lockerId = 0,
            name = "",
            expirationDate = null,
            purchaseDate = null,
            memo = null,
            imageUrl = null,
            itemCategory = ItemCategory.NONE,
            tags = null,
            count = 0,
        )
        fun createEmptyItem(position: Position) = Item(
            id = 0,
            lockerId = 0,
            name = "",
            expirationDate = null,
            purchaseDate = null,
            memo = null,
            imageUrl = null,
            itemCategory = ItemCategory.NONE,
            tags = null,
            count = 0,
            position = position
        )
    }

    @Parcelize
    data class Position(
        @androidx.annotation.FloatRange(from = 0.0, to = 100.0) val x: Float,
        @androidx.annotation.FloatRange(from = 0.0, to = 100.0) val y: Float
    ): Parcelable

    var itemFocusHandler: ItemFocusHandler = { }

    fun applyItemFocus(isFocused: Boolean) = itemFocusHandler(isFocused)

}

enum class ItemCategory(
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int
) {
    LIVING(R.string.item_category_living, R.drawable.ic_marker_living),
    FOOD(R.string.item_category_food, R.drawable.ic_marker_food),
    FASHION(R.string.item_category_fashion, R.drawable.ic_marker_fashion),
    NONE(R.string.item_category_none, R.drawable.ic_marker_none);
}
