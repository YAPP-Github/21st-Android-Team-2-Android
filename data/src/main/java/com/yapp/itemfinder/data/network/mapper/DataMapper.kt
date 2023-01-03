package com.yapp.itemfinder.data.network.mapper

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yapp.itemfinder.domain.di.ApiGsonQualifier
import com.yapp.itemfinder.domain.model.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class DataMapper @Inject constructor(
    @ApiGsonQualifier
    private val apiGson: Gson,
) {

    fun map(json: JsonObject): Data? =
        when (json.get("type").asString) {
            CellType.EMPTY_CELL.name -> convertJsonType(json, Data::class)
            CellType.CATEGORY_CELL.name -> convertJsonType(json, Category::class)
            CellType.LIKE_CELL.name -> convertJsonType(json, LikeItem::class)
            CellType.LOCKER_CELL.name -> convertJsonType(json, Locker::class)
            else -> null
        }

    private fun convertJsonType(json: JsonObject, clazz: KClass<out Data>): Data {
        return apiGson.fromJson(json.toString(), clazz.java)
    }
}
