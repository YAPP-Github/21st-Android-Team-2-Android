package com.yapp.itemfinder.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class Data(
    open var id: Long = 0,
    open var type: CellType = CellType.EMPTY_CELL,
) : Parcelable {

    var handler: DataHandler = { }
}

data class Category(
    var name: String = "default_name"
) : Data() {

    fun goCategoryPage() = handler.invoke(this)
}