package com.yapp.itemfinder.feature.common.datalist.binder

import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.feature.common.BaseViewModel

interface TagBinder {
    fun bindData(data: Tag, viewModel: BaseViewModel)
}
