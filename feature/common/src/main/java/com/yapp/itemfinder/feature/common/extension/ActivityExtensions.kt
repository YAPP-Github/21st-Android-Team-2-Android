package com.yapp.itemfinder.feature.common.extension

import android.app.Activity
import android.widget.Toast

fun Activity.showLongToast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
fun Activity.showShortToast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
