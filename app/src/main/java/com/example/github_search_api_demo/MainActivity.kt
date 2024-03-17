package com.example.github_search_api_demo

import android.os.Bundle
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.github_search_api_demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
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
    }
}