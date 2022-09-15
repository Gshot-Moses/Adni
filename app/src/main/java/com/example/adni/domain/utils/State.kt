package com.example.adni.domain.utils

sealed class State<T>

data class DataState<T>(var loading: Boolean, val data: T?, val error: String?): State<T>()

fun <T> isLoading(): DataState<T> {
    return DataState(loading = true, data = null, error = null)
}
fun <T> onResult(data: T?): DataState<T> {
    return DataState(loading = false, data = data, error = null)
}
fun <T> onError(error: String?): DataState<T> {
    return DataState(loading = false, data = null, error = error)
}

sealed class Result<T> {
    class Loading<T>(): Result<T>()
    class Success<T>(val data: T): Result<T>()
    class Failure<T>(val error: String): Result<T>()
}