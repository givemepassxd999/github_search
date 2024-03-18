package com.example.github_search_api_demo.ui

import android.os.Bundle
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.github_search_api_demo.databinding.ActivityMainBinding
import com.example.github_search_api_demo.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.queryRepos(query = "aaa")
        with(binding.searchView) {
            editText.addTextChangedListener {

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
        }
    }
}