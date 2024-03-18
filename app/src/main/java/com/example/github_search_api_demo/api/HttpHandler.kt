package com.example.github_search_api_demo.api

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class HttpHandler {
    protected suspend fun <T : Any> getResult(res: suspend () -> Response<T>): HttpResult<T> {
        try {
            val response = res()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    return HttpResult.Success(body)
                }
            }
            return HttpResult.Error(HttpException(response))
        } catch (e: IOException) {
            return HttpResult.Error(e)
        } catch (e: HttpException) {
            return HttpResult.Error(e)
        } catch (e: Exception) {
            return HttpResult.Error(java.lang.Exception("發生錯誤： ${e.message}"))
        }
    }
}