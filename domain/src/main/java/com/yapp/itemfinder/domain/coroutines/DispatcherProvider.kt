package com.yapp.itemfinder.domain.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val mainImmediate: CoroutineDispatcher

}
