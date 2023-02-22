package com.yapp.itemfinder.domain.repository

import com.yapp.itemfinder.domain.model.Tag

interface TagRepository {

    suspend fun fetchTags(): List<Tag>

    suspend fun createTags(tags: List<String>): List<Tag>

}
