package com.yapp.itemfinder.space.lockerdetail.itemfilter

import android.os.Parcelable
import androidx.annotation.StringRes
import com.yapp.itemfinder.domain.model.ItemCategory
import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.space.R
import kotlinx.parcelize.Parcelize

/**
 * 정렬, 카테고리, 태그
 */
@Parcelize
data class ItemFilterCondition(
    val order: Order,
    val itemCategoris: List<ItemCategory>,
    val tags: List<Tag>,
): Parcelable {

    enum class Order(@StringRes val nameId: Int) {
        RECENT(R.string.order_recent),
        OLD(R.string.order_old),
        ASC(R.string.order_asc),
        DESC(R.string.order_desc),
    }

    companion object {

        val NONE = ItemFilterCondition(
            order = Order.RECENT,
            itemCategoris = listOf(),
            tags = listOf()
        )

    }

}
