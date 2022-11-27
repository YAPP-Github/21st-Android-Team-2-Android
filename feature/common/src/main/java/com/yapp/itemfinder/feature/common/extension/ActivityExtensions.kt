package com.yapp.itemfinder.feature.common.extension

import android.view.View
import androidx.activity.ComponentActivity

fun ComponentActivity.findContentView(): View? =
    findViewById(android.R.id.content)
