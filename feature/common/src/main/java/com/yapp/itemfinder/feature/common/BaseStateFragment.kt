package com.yapp.itemfinder.feature.common

import androidx.viewbinding.ViewBinding

abstract class BaseStateFragment<VM : BaseStateViewModel<*, *>, VB : ViewBinding> : BaseFragment<VM, VB>()
