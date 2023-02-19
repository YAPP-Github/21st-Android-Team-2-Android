package com.yapp.itemfinder.space.additem.addtag

import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.feature.common.State

sealed class AddTagState : State {
    object Uninitialized : AddTagState()

    object Loading : AddTagState()

    data class Success(
        val selectedTagList: List<Tag>?,
        val dataList: List<Data>,
    ) : AddTagState()

    data class Error(
        val e: Throwable
    ) : AddTagState()
}
