package com.yapp.itemfinder.feature.common.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.DisplayMetrics
import androidx.appcompat.content.res.AppCompatResources
fun Int.dpToPx(context: Context): Int{
    return this * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT).toInt()
}


fun IntRange.size() = this.last - this.first + 1

fun Int.isOdd() = this % 2 == 1

fun Int.isEven() = this % 2 == 0

fun String.toDrawable(context: Context): Drawable? {
    return AppCompatResources.getDrawable(context, context.resources.getIdentifier(this.lowercase(), "drawable", context.packageName))

}

fun List<String>.toUriList() = this.map { Uri.parse(it) }

fun List<Uri>.toStringList() = this.map { it.toString() }

fun Uri.toBitMap(context: Context): Bitmap{
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source =ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver,this)
    }
    return bitmap
}

fun Bitmap.crop(): Bitmap {
    val bitmapWidth = this.width
    val bitmapHeight = this.height
    var cropStartX = 0
    var cropStartY = 0
    var cropWidth = bitmapWidth
    var cropHeight = (bitmapWidth * (3.0.div(4.0))).toInt()

    if (cropHeight > bitmapHeight){
        cropHeight = bitmapHeight
        cropWidth = (bitmapHeight * (3.0.div(4.0))).toInt()
        cropStartX = (bitmapWidth - cropWidth).div(2.0).toInt()
    }else{
        cropStartY = (bitmapHeight - cropHeight).div(2.0).toInt()
    }


    val croppedBitmap = Bitmap.createBitmap(
        this,
        cropStartX,
        cropStartY,
        cropWidth,
        cropHeight
    )

    return croppedBitmap

}

