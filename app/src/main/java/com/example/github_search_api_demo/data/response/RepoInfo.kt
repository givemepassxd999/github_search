package com.example.github_search_api_demo.data.response

import com.google.gson.annotations.SerializedName

data class RepoInfo(
    @SerializedName("total_count") var totalCount: Int,
    @SerializedName("incomplete_results") var incompleteResults: Boolean,
    @SerializedName("items") var items: List<ItemInfo>
)

data class ItemInfo(
    @SerializedName("id") var id: Int,
    @SerializedName("full_name") var fullName: String,
    @SerializedName("description") var description: String,
    @SerializedName("owner") var owner: Owner
)

data class Owner(
    @SerializedName("url") var url: String,
    @SerializedName("avatar_url") var avatarUrl: String,
)
