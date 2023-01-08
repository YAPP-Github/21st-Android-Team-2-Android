package com.yapp.itemfinder.feature.common.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.hideSoftInput() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}

fun View.gone(){
    visibility = View.GONE
}
