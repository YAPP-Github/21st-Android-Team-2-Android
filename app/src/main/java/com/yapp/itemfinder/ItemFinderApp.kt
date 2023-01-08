package com.yapp.itemfinder

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ItemFinderApp: Application() {

    override fun onCreate() {
        super.onCreate()

        initKakaoSDK()
    }

    private fun initKakaoSDK() {
        // Kakao SDK 초기화
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }

}
