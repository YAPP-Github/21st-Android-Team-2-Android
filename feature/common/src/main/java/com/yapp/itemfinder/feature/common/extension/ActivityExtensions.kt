package com.yapp.itemfinder.feature.common.extension

import android.app.Activity
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Activity.setStatusBarColor(@ColorRes statusBarColorId: Int) {
    window.statusBarColor = ContextCompat.getColor(this, statusBarColorId)
}
