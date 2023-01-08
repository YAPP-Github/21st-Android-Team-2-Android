package com.yapp.itemfinder.domain.type

enum class GenderType {
    MALE, FEMALE;

    companion object {

        fun fromKakaoGender(kakaoGender: String?): GenderType? =
            when (kakaoGender) {
                MALE.name -> MALE
                FEMALE.name -> FEMALE
                else -> null
            }

    }
}
