package com.yapp.itemfinder.data.network.api.managespace

import com.google.gson.annotations.SerializedName

data class AddSpaceRequest(
    @SerializedName("name") val name: String
)
