package com.example.github_search_api_demo.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.github_search_api_demo.R
import com.example.github_search_api_demo.ui.MainApplication
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException

object ApiManager {

    private const val BASE_URL = "https://api.github.com/"
    private val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private const val cacheSize = (5 * 1024 * 1024).toLong()
    private val client = OkHttpClient.Builder()
        .cache(Cache(MainApplication.getApplication().cacheDir, cacheSize))
        .addInterceptor(logger)
        .addInterceptor(NoInternetInterceptor())
        .addInterceptor { chain ->
            var request = chain.request()
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                .build()
            chain.proceed(request)
        }
        .build()

    fun create(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

class NoInternetInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (isNetworkAvailable(MainApplication.getApplication()).not()) {
            Timber.d("NoInternetInterceptor")
            throw NoNetworkException(MainApplication.getApplication())
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                ) {
                    return true
                }
                false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                if (isConnected) {
                    return true
                }
                false
            }
        }
        return false
    }
}

class NoNetworkException(private val context: Context) : IOException() {
    override val message: String
        get() = context.getString(R.string.no_internet_connection_available)
}

