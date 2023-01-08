package com.yapp.itemfinder

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ItemFinderApp: Application() {

    override fun onCreate() {
        super.onCreate()

        initKakaoSDK()
    }

    private fun initKakaoSDK() {

        val keyHash: String = Utility.getKeyHash(this /* context */)
        Log.e("keyHash", keyHash)
        // Kakao SDK 초기화
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }

}
