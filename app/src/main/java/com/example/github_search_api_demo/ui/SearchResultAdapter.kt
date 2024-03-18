package com.example.github_search_api_demo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.github_search_api_demo.data.response.ItemInfo
import com.example.github_search_api_demo.databinding.ItemInfoBinding

class SearchResultAdapter : PagingDataAdapter<ItemInfo, RecyclerView.ViewHolder>(ItemComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemInfoBinding.inflate(layoutInflater, parent, false)
        return ItemInfoHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { item ->
            (holder as ItemInfoHolder).bind(item)
        }
    }

    class ItemInfoHolder(private val binding: ItemInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemInfo) {
            binding.itemTitle.text = item.fullName
        }
    }
}

class ItemComparator : DiffUtil.ItemCallback<ItemInfo>() {
    override fun areItemsTheSame(oldItem: ItemInfo, newItem: ItemInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemInfo, newItem: ItemInfo): Boolean {
        return oldItem.fullName == newItem.fullName
    }

}