package com.example.github_search_api_demo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.github_search_api_demo.data.repository.Repository
import com.example.github_search_api_demo.data.response.ItemInfo
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException


const val START_INDEX = 0

class ItemInfoPagingSource(private val repo: Repository, private val queryKey: String) :
    PagingSource<Int, ItemInfo>() {

    override fun getRefreshKey(state: PagingState<Int, ItemInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemInfo> {
        return try {
            val position = params.key ?: START_INDEX
            val response =
                repo.queryRepos(queryKey = queryKey, page = position, pageCount = PAGE_SIZE)
            val prevKey = if (position == START_INDEX) {
                null
            } else {
                position - 1
            }
            val nextKey = if (response.isEmpty()) {
                null
            } else {
                position + 1
            }
            LoadResult.Page(
                data = response,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            Timber.d("IOException")
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Timber.d("HttpException")
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            Timber.d("Exception")
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val PAGE_SIZE = 100
    }
}