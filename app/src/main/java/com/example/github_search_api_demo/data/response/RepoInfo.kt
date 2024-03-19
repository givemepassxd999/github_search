package com.example.github_search_api_demo.data.response

import com.google.gson.annotations.SerializedName

data class RepoInfo(
    @SerializedName("items") var items: List<ItemInfo>? = null
)

data class ItemInfo(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("full_name") var fullName: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("owner") var owner: Owner? = null
)

data class Owner(
    @SerializedName("html_url") var url: String? = null,
    @SerializedName("avatar_url") var avatarUrl: String? = null,
)
