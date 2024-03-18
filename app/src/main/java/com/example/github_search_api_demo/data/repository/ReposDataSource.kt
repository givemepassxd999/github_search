package com.example.github_search_api_demo.data.repository

import com.example.github_search_api_demo.api.ApiService
import com.example.github_search_api_demo.api.HttpHandler
import com.example.github_search_api_demo.api.HttpResult
import com.example.github_search_api_demo.data.repository.Repository
import com.example.github_search_api_demo.data.response.ItemInfo

class ReposDataSource(private val service: ApiService) : HttpHandler(), Repository {

    override suspend fun queryRepos(key: String, page: Int): HttpResult<ItemInfo> {
        return getResult { service.getRepository(key, page) }
    }
}