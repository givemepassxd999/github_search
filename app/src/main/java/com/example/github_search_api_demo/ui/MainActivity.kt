package com.example.github_search_api_demo.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.github_search_api_demo.R
import com.example.github_search_api_demo.databinding.ActivityMainBinding
import com.example.github_search_api_demo.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchResultAdapter
    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initSearchView()
        with(binding.searchView) {
            editText.addTextChangedListener {
                val info = it.toString()
                if (info.isEmpty()) return@addTextChangedListener
                queryData(info)
            }
            editText.setOnEditorActionListener { v, _, _ ->
                val info = (v as EditText).text.toString()
                binding.searchBar.setText(info)
                hide()
                queryData(info)
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
        }
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                val refreshState = loadState.refresh
                binding.searchResultList.isVisible = refreshState is LoadState.NotLoading

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
            mainViewModel.queryRepos(info).collectLatest {
                binding.searchDataList.adapter?.let { adapter ->
                    (adapter as SearchResultAdapter).submitData(it)
                }
                binding.searchResultList.adapter?.let { adapter ->
                    (adapter as SearchResultAdapter).submitData(it)
                }
            }
        }
    }

    private fun initSearchView() {
        adapter = SearchResultAdapter()
        binding.searchResultList.adapter = adapter
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.searchResultList.addItemDecoration(decoration)
        binding.searchDataList.adapter = SearchResultAdapter()
    }
}