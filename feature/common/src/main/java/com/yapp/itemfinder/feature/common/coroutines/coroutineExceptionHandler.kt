package com.yapp.itemfinder.feature.common.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler

val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    //DefaultErrorHandler.handle(throwable)
}
