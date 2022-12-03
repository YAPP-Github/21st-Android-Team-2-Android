package com.yapp.itemfinder.data.network.mapper

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yapp.itemfinder.domain.model.CellType
import com.yapp.itemfinder.domain.model.Data
import com.yapp.itemfinder.domain.model.Food
import kotlin.reflect.KClass

object DataMapper {

  private var gson: Gson? = null
  private var type: String? = null

  fun map(json: JsonObject): Data? {
    type = json.get("type").asString

    return when (type) {
      CellType.EMPTY_CELL.name -> convertJsonType(json, Data::class)
      CellType.FOOD.name -> convertJsonType(json, Food::class)
      else -> null
    }
  }

  private fun convertJsonType(json: JsonObject, clazz: KClass<out Data>): Data {
    return gson!!.fromJson(json.toString(), clazz.java)
  }
}
