package com.yapp.itemfinder.domain.provider

import android.content.Context
import android.content.Intent

interface AppNavigateProvider {

    fun newIntentForPrelogin(context: Context): Intent

    fun newIntentForOssPlugin(context: Context): Intent

}
