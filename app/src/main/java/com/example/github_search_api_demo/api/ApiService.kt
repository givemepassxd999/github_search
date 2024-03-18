package com.example.github_search_api_demo.api

import com.example.github_search_api_demo.data.response.ItemInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/repositories")
    suspend fun getRepository(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 10
    ): Response<ItemInfo>
}