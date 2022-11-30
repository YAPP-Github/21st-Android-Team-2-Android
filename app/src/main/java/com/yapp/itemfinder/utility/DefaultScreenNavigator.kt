package com.yapp.itemfinder.utility

import android.content.Context
import android.content.Intent
import com.yapp.itemfinder.feature.common.utility.ScreenNavigator
import com.yapp.itemfinder.home.HomeActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultScreenNavigator @Inject constructor(): ScreenNavigator {

    override fun newIntentHomeActivity(context: Context): Intent =
        HomeActivity.newIntent(context)

}
