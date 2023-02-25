package com.yapp.itemfinder.domain.data

enum class PrefName(val value: String, val alias: String) {
    // 회원가입 - 회원탈퇴까지 활용되는 정보
    User("USER_PREFERENCES", "USER_PREFERENCES_SECURITY_ALIAS"),
}

sealed class SecureLocalData<T> {
    abstract val prefName: PrefName
    abstract val key: String
    abstract val default: T

    // primitive type이 아닌 경우 타입추론을 위해 사용합니다.
    open var complexType: Class<T>? = null

    object KakaoSocialId : SecureLocalData<String>() {
        override val prefName: PrefName = PrefName.User
        override val key: String = "pref_kakao_social_id"
        override val default: String = ""
    }

    object RefreshToken : SecureLocalData<String>() {
        override val prefName: PrefName = PrefName.User
        override val key: String = "pref_refresh_token"
        override val default: String = ""
    }

    object AccessToken : SecureLocalData<String>() {
        override val prefName: PrefName = PrefName.User
        override val key: String = "pref_access_token"
        override val default: String = ""
    }

    object Nickname : SecureLocalData<String>() {
        override val prefName: PrefName = PrefName.User
        override val key: String = "pref_user_nickname"
        override val default: String = ""
    }

    object SocialId : SecureLocalData<String>() {
        override val prefName: PrefName = PrefName.User
        override val key: String = "pref_user_social_id"
        override val default: String = ""
    }


}
