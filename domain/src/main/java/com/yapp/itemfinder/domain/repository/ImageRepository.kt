package com.yapp.itemfinder.domain.repository

interface ImageRepository {
    suspend fun addImages(jpegImagePath: List<String>): List<String>
}
