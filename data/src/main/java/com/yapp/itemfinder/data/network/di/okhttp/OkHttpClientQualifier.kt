package com.yapp.itemfinder.data.network.di.okhttp

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OkHttpClientQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OkHttpClientWithoutAuthenticatorQualifier
