package com.example.github_search_api_demo.di

import com.example.github_search_api_demo.api.ApiManager
import com.example.github_search_api_demo.data.repository.Repository
import com.example.github_search_api_demo.data.repository.RepositoryImpl
import com.example.github_search_api_demo.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppModule {
    val appModule = module {
        single {
            ApiManager.create()
        }
    }
    val vmModule = module {
        viewModel { MainViewModel(get()) }
    }
    val repoModule = module {
        single<Repository> { RepositoryImpl(get()) }
    }
}