package com.yapp.itemfinder.testbase

import org.mockito.Mockito

fun <T> any(): T {
    Mockito.any<T>()
    return null as T
}

fun <T> eq(value: T): T {
    Mockito.eq(value)
    return null as T
}
