package com.example.github_search_api_demo.data.repository

import com.example.github_search_api_demo.data.response.ItemInfo

interface Repository {
    suspend fun queryRepos(page: Int, queryKey: String, pageCount: Int): List<ItemInfo>
}