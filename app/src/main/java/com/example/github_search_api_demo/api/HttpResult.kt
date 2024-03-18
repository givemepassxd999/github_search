package com.example.github_search_api_demo.api

sealed class HttpResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : HttpResult<T>()
    data class Error(val exception: Throwable) : HttpResult<Nothing>()
}