package com.example.github_search_api_demo.data.repository

import com.example.github_search_api_demo.api.ApiService
import com.example.github_search_api_demo.api.HttpHandler
import com.example.github_search_api_demo.api.HttpResult
import com.example.github_search_api_demo.data.response.ItemInfo

class RepositoryImpl(private val service: ApiService) : Repository {
    private val httpHandler = HttpHandler()
    override suspend fun queryRepos(index: Int, queryKey: String, pageCount: Int): List<ItemInfo> {
        val result = httpHandler.getResult {
            service.getRepository(
                page = index,
                queryKey = queryKey,
                pageCount = pageCount
            )
        }
        return when (result) {
            is HttpResult.Success -> result.data.items
            is HttpResult.Error -> throw result.exception
        }
    }
}