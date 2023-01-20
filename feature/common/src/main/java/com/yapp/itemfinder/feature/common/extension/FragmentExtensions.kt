package com.yapp.itemfinder.feature.common.extension

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.setStatusBarColor(@ColorRes statusBarColorId: Int) {
    requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), statusBarColorId)
}
