package com.yapp.itemfinder.feature.common.exception

import kotlinx.coroutines.CoroutineExceptionHandler

val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    DefaultErrorHandler.handle(throwable)
}
