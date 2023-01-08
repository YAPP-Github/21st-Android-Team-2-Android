package com.yapp.itemfinder.feature.common.utility

import com.yapp.itemfinder.domain.model.Data

data class DataWithSpan<D: Data>(
    val data:Data,
    val span: Int
)
