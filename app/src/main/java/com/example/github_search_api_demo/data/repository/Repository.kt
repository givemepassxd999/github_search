package com.example.github_search_api_demo.data.repository

import com.example.github_search_api_demo.api.HttpResult
import com.example.github_search_api_demo.data.response.ItemInfo

interface Repository {
    suspend fun queryRepos(key: String, page: Int): HttpResult<ItemInfo>
}