package com.yapp.itemfinder.feature.common.adaper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.yapp.itemfinder.feature.common.databinding.ItemImageViewholderItemBinding

class ItemImageAdapter : ListAdapter<String, ItemImageViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemImageViewHolder {
        val binding = ItemImageViewholderItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ItemImageViewHolder(val binding: ItemImageViewholderItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context
    fun bind(data: String) {
        var a : Int = 0
        var b: Int=0
        Glide.with(context).load(data).into(binding.imageView).getSize { width, height ->
//            a = width.also { println() }
//            b = height.also { println() }
        }
//        a++
//        b++
    }
}
