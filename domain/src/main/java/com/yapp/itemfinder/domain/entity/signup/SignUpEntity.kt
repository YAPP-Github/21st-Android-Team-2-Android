package com.yapp.itemfinder.domain.entity.signup

import com.yapp.itemfinder.domain.type.GenderType
import com.yapp.itemfinder.domain.type.SocialType

data class SignUpEntity(
    val socialId: String,
    val socialType: SocialType,
    val nickname: String,
    val email: String?,
    val gender: GenderType?,
    val birthYear: Int?
)
