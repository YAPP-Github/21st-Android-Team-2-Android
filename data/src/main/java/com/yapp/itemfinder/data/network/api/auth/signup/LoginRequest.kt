package com.yapp.itemfinder.data.network.api.auth.signup

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("socialId") val socialId: String,
    @SerializedName("socialType") val socialType: String,
)
