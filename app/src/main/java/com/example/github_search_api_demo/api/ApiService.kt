package com.example.github_search_api_demo.api

import com.example.github_search_api_demo.data.response.ItemInfo
import com.example.github_search_api_demo.data.response.RepoInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/repositories")
    suspend fun getRepository(
        @Query("page") page: Int,
        @Query("q") queryKey: String,
        @Query("per_page") pageCount: Int = 10
    ): Response<RepoInfo>
}