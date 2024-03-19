package com.example.github_search_api_demo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github_search_api_demo.data.response.ItemInfo
import com.example.github_search_api_demo.databinding.SearchResultItemInfoBinding

class SearchResultAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<ItemInfo, RecyclerView.ViewHolder>(ItemComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SearchResultItemInfoBinding.inflate(layoutInflater, parent, false)
        return ItemInfoHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { item ->
            (holder as ItemInfoHolder).bind(item)
        }
    }

    inner class ItemInfoHolder(private val binding: SearchResultItemInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemInfo) {
            binding.itemTitle.text = item.fullName
            binding.itemDescription.text = item.description
            item.owner?.avatarUrl?.let { url ->
                Glide.with(binding.root)
                    .load(url)
                    .into(binding.repoAvatar)
            }
            itemView.setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<ItemInfo>() {
        override fun areItemsTheSame(oldItem: ItemInfo, newItem: ItemInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ItemInfo, newItem: ItemInfo): Boolean {
            return oldItem.id == newItem.id
        }

    }
}

fun interface OnItemClickListener {
    fun onItemClick(item: ItemInfo)
}



