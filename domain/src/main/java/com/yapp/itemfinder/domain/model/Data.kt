package com.yapp.itemfinder.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class Data(
    open val id: Long = 0,
    open val type: CellType = CellType.EMPTY_CELL,
) : Parcelable {

    var handler: DataHandler = { }

}
