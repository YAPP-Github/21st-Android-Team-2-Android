package com.yapp.itemfinder.feature.common.coroutines

import com.yapp.itemfinder.feature.common.exception.DefaultErrorHandler
import kotlinx.coroutines.CoroutineExceptionHandler

val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    DefaultErrorHandler.handle(throwable)
}
