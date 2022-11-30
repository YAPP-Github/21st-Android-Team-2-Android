package com.yapp.itemfinder.feature.common.utility

import android.content.Context
import android.content.Intent

interface ScreenNavigator {

    fun newIntentHomeActivity(context: Context): Intent

}
