package com.yapp.itemfinder.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpaceAndLockerEntity(
    val manageSpaceEntity: ManageSpaceEntity,
    val lockerEntity: LockerEntity?,
): Parcelable
