package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.tag.CreateTagsRequest
import com.yapp.itemfinder.data.network.api.tag.TagApi
import com.yapp.itemfinder.domain.model.Tag
import com.yapp.itemfinder.domain.repository.TagRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepositoryImpl @Inject constructor(
    private val tagApi: TagApi
): TagRepository {

    override suspend fun fetchTags(): List<Tag> =
        tagApi.fetchTags().tags.map { it.toEntity() }

    override suspend fun createTags(tags: List<String>): List<Tag> =
        tagApi.createTags(CreateTagsRequest(tags)).tags.map { it.toEntity() }

}
