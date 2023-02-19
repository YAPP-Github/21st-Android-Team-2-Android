package com.yapp.itemfinder.space.additem.addtag

import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.feature.common.SideEffect

sealed class AddTagSideEffect : SideEffect {

    data class AddTags(val tagList: List<Tag>): AddTagSideEffect()

}
