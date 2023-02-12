package com.yapp.itemfinder.data.repositories

import android.content.Context
import com.yapp.itemfinder.data.R
import com.yapp.itemfinder.data.network.api.image.ImageApi
import com.yapp.itemfinder.domain.repository.ImageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    @ApplicationContext private val context: Context
) : ImageRepository {
    override suspend fun addImages(jpedImagePaths: List<String>): List<String> {
        val imageMultipartBodies = jpedImagePaths.map {path ->
            val file = File(path)
            val fileRequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(context.getString(R.string.multipart_image_key),file.name,fileRequestBody )
        }
        return imageApi.addImages(imageMultipartBodies).map { it.url }
    }
}
