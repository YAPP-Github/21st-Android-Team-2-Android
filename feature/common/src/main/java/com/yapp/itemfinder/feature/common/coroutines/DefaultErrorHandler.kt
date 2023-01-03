package com.yapp.itemfinder.feature.common.coroutines

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.yapp.itemfinder.feature.common.BuildConfig
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

object DefaultErrorHandler {

    fun handle(e: Throwable) {
        // DEBUG면 모든 에러의 stack trace를 print 한다.
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }

        if (isRequiredToMonitorAsNonFatalException(e)) {
            try {
                FirebaseCrashlytics.getInstance().recordException(e)
            } catch (_: Throwable) {
                // Implicit Empty
            }
        }
    }

    fun isNetworkException(e: Throwable) = e is HttpException

    fun isNetworkUnavailableException(e: Throwable) = e is UnknownHostException || e is ConnectException

    fun isTokenExpiredException(e: Throwable) = e is HttpException && e.code() == 401

    fun isJobCancellationException(e: Throwable) = e is CancellationException

    private fun isRequiredToMonitorAsNonFatalException(rawException: Throwable): Boolean {
        val exception = (rawException as? Exception)?.cause ?: rawException
        return (
            isNetworkException(exception) ||
                isNetworkUnavailableException(exception) ||
                isJobCancellationException(exception)
            ).not()
    }

}
