package com.yapp.itemfinder.provider

import android.content.Context
import android.content.Intent
import com.yapp.itemfinder.domain.provider.AppNavigateProvider
import com.yapp.itemfinder.prelogin.PreloginActivity
import javax.inject.Inject


class DefaultAppNavigateProvider @Inject constructor() : AppNavigateProvider{

    override fun newIntentForPrelogin(context: Context): Intent =
        PreloginActivity.newIntent(context)
}
