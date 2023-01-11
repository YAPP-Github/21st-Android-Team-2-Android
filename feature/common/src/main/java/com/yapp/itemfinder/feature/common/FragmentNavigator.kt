package com.yapp.itemfinder.feature.common

interface FragmentNavigator {
    fun showFragment(tag: String)
    fun addFragmentBackStack(tag: String)
}
