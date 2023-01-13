package com.yapp.itemfinder.feature.common.extension

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import androidx.appcompat.content.res.AppCompatResources

fun Int.dpToPx(context: Context): Int{
    return this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT).toInt()
}


fun IntRange.size() = this.last - this.first + 1

fun Int.isOdd() = this % 2 == 1

fun Int.isEven() = this % 2 == 0

fun String.toDrawable(context: Context): Drawable? {
    return AppCompatResources.getDrawable(context, context.resources.getIdentifier(this, "drawable", context.packageName))

}
