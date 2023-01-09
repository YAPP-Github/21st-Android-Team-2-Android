package com.yapp.itemfinder.feature.common.extension

import android.content.Context
import android.util.DisplayMetrics

fun Int.dpToPx(context: Context): Int{
    return this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT).toInt()
}


fun IntRange.size() = this.last - this.first + 1

fun Int.isOdd() = this % 2 == 1

fun Int.isEven() = this % 2 == 0
