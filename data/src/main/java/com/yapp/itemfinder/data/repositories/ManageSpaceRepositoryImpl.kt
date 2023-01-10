package com.yapp.itemfinder.data.repositories

import com.yapp.itemfinder.data.network.api.managespace.AddSpaceRequest
import com.yapp.itemfinder.data.network.api.managespace.ManageSpaceApi
import com.yapp.itemfinder.data.network.mapper.DataMapper
import com.yapp.itemfinder.domain.coroutines.DispatcherProvider
import com.yapp.itemfinder.domain.model.ManageSpaceItem
import com.yapp.itemfinder.domain.repository.ManageSpaceRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManageSpaceRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val dataMapper: DataMapper,
    private val api: ManageSpaceApi
) : ManageSpaceRepository {

    override suspend fun getAllManageSpaceItems(): List<ManageSpaceItem> =
        withContext(dispatcherProvider.io) {
            val jsonObject = api.fetchAllSpaces().body()
            val spaceList: MutableList<ManageSpaceItem> = mutableListOf()
            jsonObject?.getAsJsonArray("spaces")?.forEach {
                spaceList.add(
                    ManageSpaceItem(
                        name = it.asJsonObject.get("name").asString,
                        id = it.asJsonObject.get("id").asLong
                    )
                )
            }
            spaceList
        }

    override suspend fun addNewSpace(name: String): Int = withContext(dispatcherProvider.io) {
        api.addNewSpace(AddSpaceRequest(name)).body()!!
    }

    override fun editSpace(): Boolean {
        //
        return true
    }

    override fun deleteSpace(): Boolean {
        TODO("Not yet implemented")
    }
}
