package com.yapp.itemfinder.feature.common

import android.os.Parcelable

interface FragmentNavigator {
    fun showFragment(tag: String)
    fun addFragmentBackStack(tag: String, bundlePair:Pair<String,Parcelable>? = null)
}
