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
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun Int.dpToPx(context: Context): Int {
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

fun Uri.toBitMap(context: Context): Bitmap {
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)
    }
    return bitmap
}

fun Uri.cropToJpeg(context: Context, widthRatio: Int, heightRatio: Int): String {
    return this.toBitMap(context).crop(widthRatio, heightRatio).reduceSize().toJpeg(context)
}

fun Bitmap.crop(widthRatio: Int, heightRatio: Int): Bitmap {
    val bitmapWidth = this.width
    val bitmapHeight = this.height
    var cropStartX = 0
    var cropStartY = 0
    var cropWidth = 0
    var cropHeight = 0

    if (widthRatio < heightRatio) { // 세로가 더 길게 크롭하는 경우, 예를 들어 가로 : 세로 = 3 : 4
        cropHeight = bitmapHeight
        cropWidth = (cropHeight * (widthRatio.toFloat().div(heightRatio.toFloat()))).toInt() // 높이 * 3/4

        if (cropWidth <= bitmapWidth) { // 정상적으로 크로핑될수 있는 경우
            cropStartX = (bitmapWidth - cropWidth).div(2.0).toInt()

        }else{ // 크로핑 후, 가로길이가 커진 경우 가로길이 기준으로 다시 설정해준다.
            cropWidth = bitmapWidth
            cropHeight = (cropWidth * (heightRatio.toFloat().div(widthRatio.toFloat()))).toInt() // 높이 * 4/3
            cropStartY = (bitmapHeight - cropHeight).div(2.0).toInt()
        }
    }else{ // widthRatio > heightRatio, 가로가 더 길게 크롭하는 경우. 예를 들어서 16: 9
        cropWidth = bitmapWidth
        cropHeight = (cropWidth * (heightRatio.toFloat().div(widthRatio.toFloat()))).toInt() // 높이 * 9/16

        if (cropHeight <= bitmapHeight){ // 정상적으로 크로핑되는 경우
            cropStartY = (bitmapHeight - cropHeight).div(2.0).toInt()
        }else{// 크로핑 후, 세로길이가 커진 경우 세로길이 기준으로 다시 설정해준다.
            cropHeight = bitmapHeight
            cropWidth = (cropHeight * (widthRatio.toFloat().div(heightRatio.toFloat()))).toInt() // 높이 * 16 / 9
            cropStartX = (bitmapWidth - cropWidth).div(2.0).toInt()
        }

    }
    return Bitmap.createBitmap(
        this,
        cropStartX,
        cropStartY,
        cropWidth,
        cropHeight
    )

}

fun Bitmap.reduceSize(): Bitmap {
    val isSquare = this.width == this.height

    if (isSquare && this.width > 1024) {
        return Bitmap.createScaledBitmap(this, 1080, 1080, false)
    }

    val ratio = this.height.toFloat().div(this.width.toFloat())
    val is4to3 = ratio in 0.72..0.78

    if (is4to3 && this.width > 1440) {
        return Bitmap.createScaledBitmap(this, 1440, 1080, false)
    }

    return this
}

fun Bitmap.toJpeg(context: Context): String {

    val storage = context.cacheDir
    val fileName = String.format("%s.%s", UUID.randomUUID(), "jpeg")
    val tempFile = File(storage, fileName).apply { createNewFile() }
    val fos = FileOutputStream(tempFile)
    this.compress(Bitmap.CompressFormat.JPEG, 70, fos)
    this.recycle()
    fos.flush()
    fos.close()
    return tempFile.absolutePath
}

