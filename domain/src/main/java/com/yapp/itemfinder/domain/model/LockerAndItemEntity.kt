package com.yapp.itemfinder.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LockerAndItemEntity(
    val lockerEntity: LockerEntity,
    val item: Item?
): Parcelable
