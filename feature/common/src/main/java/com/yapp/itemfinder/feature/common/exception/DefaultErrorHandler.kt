package com.yapp.itemfinder.feature.common.exception

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.yapp.itemfinder.data.network.response.ErrorCode
import com.yapp.itemfinder.data.network.response.ErrorResultEntity
import com.yapp.itemfinder.feature.common.BuildConfig
import okhttp3.ResponseBody
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

    fun parseErrorResult(e: Throwable): ErrorResultEntity =
        parseNetworkErrorResultInternal(e)
            ?: e.cause?.let { parseNetworkErrorResultInternal(it) }
            ?: if (e is UnknownHostException || e is ConnectException) {
                ErrorResultEntity(
                    code = ErrorCode.CAN_NOT_CONNECT.code,
                    message = null,
                )
            } else {
                ErrorResultEntity(
                    code = ErrorCode.UNKNOWN.code,
                    message = null,
                )
            }

    private fun parseNetworkErrorResultInternal(e: Throwable): ErrorResultEntity? =
        if (e is HttpException) {
            e.response()?.errorBody()?.let { responseBody ->
                parseNetworkErrorBodyResultInternal(responseBody)
            }
        } else null

    private fun parseNetworkErrorBodyResultInternal(responseBody: ResponseBody): ErrorResultEntity {
        val rawJson = responseBody.string()
        return Gson().fromJson(rawJson, ErrorResultEntity::class.java)
    }

    fun isNetworkException(e: Throwable) = e is HttpException

    fun isNetworkUnavailableException(e: Throwable) = e is UnknownHostException || e is ConnectException

    fun isTokenExpiredException(e: Throwable) = e is HttpException && e.code() == 401

    fun isApiNotFoundException(e: Throwable) = e is HttpException && e.code() == 404

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
