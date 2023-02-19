package com.yapp.itemfinder.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tag(
    override val id: Long,
    val name: String,
) : Data(id), Parcelable
