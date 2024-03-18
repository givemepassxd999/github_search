package com.example.github_search_api_demo.api

import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class HttpHandler {
    suspend fun <T : Any> getResult(res: suspend () -> Response<T>): HttpResult<T> {
        try {
            val response = res()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Timber.d("HttpHandler: getResult: body: $body")
                    return HttpResult.Success(body)
                }
            }
            Timber.d("HttpHandler: getResult: response: $response")
            return HttpResult.Error(HttpException(response))
        } catch (e: IOException) {
            Timber.d("HttpHandler: getResult: IOException: $e")
            return HttpResult.Error(e)
        } catch (e: HttpException) {
            Timber.d("HttpHandler: getResult: HttpException: $e")
            return HttpResult.Error(e)
        } catch (e: Exception) {
            Timber.d("HttpHandler: getResult: Exception: $e")
            return HttpResult.Error(java.lang.Exception("發生錯誤： ${e.message}"))
        }
    }
}