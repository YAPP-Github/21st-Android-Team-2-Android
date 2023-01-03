package com.yapp.itemfinder.feature.common.extension

import com.yapp.itemfinder.data.network.response.ErrorResultEntity
import com.yapp.itemfinder.feature.common.exception.DefaultErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Performs the given [action] on the encapsulated [ErrorWithResultException] exception if this instance represents [failure][Result.isFailure].
 * Returns the original `Result` unchanged.
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.onErrorWithResult(action: (exception: ErrorWithResultException) -> Unit): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    exceptionOrNull()?.let { action(ErrorWithResultException(it)) }
    return this
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 */
inline fun <T, R> T.runCatchingWithErrorHandler(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        DefaultErrorHandler.handle(e)
        Result.failure(e)
    }
}

/**
 * ```
 * flowOf<String>()
 *    .catchErrorWithResult { e: ErrorWithResultException ->
 *        e.errorResultEntity
 *    }.collect()
 * ```
 */

fun <T> Flow<T>.catchErrorWithResult(action: suspend FlowCollector<T>.(ErrorWithResultException) -> Unit): Flow<T> = catch { error ->
    action(ErrorWithResultException(error))
}

/**
 * ```
 * flowOf<String>()
 *    .catchWithErrorHandler {
 *      // catches exceptions in emit data
 *    }.collect()
 * ```
 */

fun <T> Flow<T>.catchWithErrorHandler(action: suspend FlowCollector<T>.(Throwable) -> Unit): Flow<T> = catch { error ->
    DefaultErrorHandler.handle(error)
    action(error)
}

data class ErrorWithResultException(val throwable: Throwable): Exception(throwable) {
    val errorInfoEntity: ErrorResultEntity
        get() = DefaultErrorHandler.parseErrorResult(throwable)
}
