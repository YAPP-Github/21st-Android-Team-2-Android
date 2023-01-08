package com.yapp.itemfinder.data.network.api.auth.login

import com.google.gson.annotations.SerializedName
import com.yapp.itemfinder.domain.entity.signup.SignUpEntity

data class SignUpRequest(
    @SerializedName("socialId") val socialId: String,
    @SerializedName("socialType") val socialType: String,
    val nickname: String?,
    val email: String?,
    val gender: String?,
    val birthYear: Int?
) {

    companion object {

        fun fromEntity(signUpEntity: SignUpEntity) = SignUpRequest(
            socialId = signUpEntity.socialId,
            socialType = signUpEntity.socialType.name,
            nickname = signUpEntity.nickname,
            email = signUpEntity.email,
            gender = signUpEntity.gender?.name,
            birthYear = signUpEntity.birthYear
        )

    }

}
