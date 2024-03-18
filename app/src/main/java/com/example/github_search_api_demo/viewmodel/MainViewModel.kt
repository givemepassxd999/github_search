package com.example.github_search_api_demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github_search_api_demo.data.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    fun queryRepos(query: String) {
        viewModelScope.launch {
            repository.queryRepos(query, PAGE_SIZE)
        }
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}