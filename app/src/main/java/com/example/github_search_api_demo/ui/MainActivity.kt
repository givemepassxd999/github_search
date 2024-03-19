package com.example.github_search_api_demo.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.github_search_api_demo.R
import com.example.github_search_api_demo.databinding.ActivityMainBinding
import com.example.github_search_api_demo.ext.debounce
import com.example.github_search_api_demo.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchResultAdapter: SearchResultAdapter
    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initView()
        with(binding.searchView) {
            lifecycleScope.launch {
                editText.debounce(1000).collectLatest {
                    binding.searchBar.setText(it)
                    queryData(it)
                    mainViewModel.setQuery(it)
                }
            }
            editText.setOnEditorActionListener { v, _, _ ->
                val info = (v as EditText).text.toString()
                binding.searchBar.setText(info)
                hide()
                false
            }

            onBackPressedDispatcher.addCallback(
                this@MainActivity,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (isShowing) {
                            hide()
                        } else {
                            finish()
                        }
                    }
                })
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            syncSearchBar(mainViewModel.currentQueryValue.value)
        }
        with(binding.searchView) {
            if (isShowing) {
                hide()
            }
        }
        syncSearchBar(mainViewModel.currentQueryValue.value)
        queryData(mainViewModel.currentQueryValue.value)
    }

    private fun syncSearchBar(info: String) {
        binding.searchBar.setText(info)
        updateState(LoadState.Loading)
    }

    private fun updateState(state: LoadState) {
        lifecycleScope.launch {
            searchResultAdapter.loadStateFlow.collect { loadState ->
                val refreshState = if (state == LoadState.Loading) {
                    loadState.refresh
                } else {
                    loadState.append
                }
                binding.searchResultList.isVisible = refreshState is LoadState.NotLoading
                binding.progressBar.isVisible = refreshState is LoadState.Loading
                binding.emptyViewLayout.isVisible =
                    refreshState is LoadState.NotLoading && searchResultAdapter.itemCount == 0

                if (refreshState is LoadState.Error) {
                    when (refreshState.error as Exception) {
                        is HttpException -> {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.internet_error), Toast.LENGTH_SHORT
                            ).show()
                        }

                        is IOException -> {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.no_internet_connection_available),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.unknown_error), Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    val errorState = loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error
                    errorState?.let {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.unknown_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun queryData(info: String) {
        lifecycleScope.launch {
            updateState(LoadState.Loading)
            mainViewModel.queryRepos(info).collectLatest { itemList ->
                searchResultAdapter.run {
                    submitData(PagingData.empty())
                    if (info.isNotEmpty()) {
                        submitData(itemList)
                    } else {
                        updateState(LoadState.NotLoading(true))
                    }
                }
            }
        }
    }

    private fun initView() {
        searchResultAdapter = SearchResultAdapter {
            it.owner?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
        binding.searchResultList.adapter = searchResultAdapter
        binding.searchDataList.adapter = searchResultAdapter
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.searchResultList.addItemDecoration(decoration)
    }
}