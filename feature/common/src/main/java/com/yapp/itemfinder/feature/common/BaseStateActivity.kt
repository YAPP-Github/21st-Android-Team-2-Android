package com.yapp.itemfinder.feature.common

import androidx.viewbinding.ViewBinding

abstract class BaseStateActivity<VM : BaseStateViewModel<*, *>, VB : ViewBinding> : BaseActivity<VM, VB>()
