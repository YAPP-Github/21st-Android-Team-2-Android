package com.yapp.itemfinder.feature.common

import android.os.Bundle
import android.os.Parcelable

interface FragmentNavigator {
    fun showFragment(tag: String)
    fun addFragmentBackStack(tag: String, bundle: Bundle? = null)
}
