package com.yapp.itemfinder.domain.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

typealias TagHandler = (Tag) -> Unit

@Parcelize
data class Tag(
    override val id: Long,
    val name: String,
) : Data(id, CellType.TAG_CELL), Parcelable {

    @IgnoredOnParcel
    var tagHandler: TagHandler = { }

    fun selectTag() = tagHandler(this)

}
