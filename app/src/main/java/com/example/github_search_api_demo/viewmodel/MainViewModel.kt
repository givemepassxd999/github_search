package com.example.github_search_api_demo.viewmodel

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.github_search_api_demo.data.paging.ItemInfoPagingSource
import com.example.github_search_api_demo.data.repository.Repository
import com.example.github_search_api_demo.data.response.ItemInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val repository: Repository) : ViewModel() {

    private var _currentQueryValue = MutableStateFlow("")
    val currentQueryValue: StateFlow<String> = _currentQueryValue

    fun setQuery(query: String) {
        _currentQueryValue.value = query
    }

    fun queryRepos(query: String): Flow<PagingData<ItemInfo>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ItemInfoPagingSource(queryKey = query, repo = repository) }
        ).flow.cachedIn(viewModelScope)
    }
}