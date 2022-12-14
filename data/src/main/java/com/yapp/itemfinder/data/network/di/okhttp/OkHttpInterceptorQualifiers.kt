package com.yapp.itemfinder.data.network.di.okhttp

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HeaderInterceptorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HttpLoggingInterceptorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DataParseInterceptorQualifier
