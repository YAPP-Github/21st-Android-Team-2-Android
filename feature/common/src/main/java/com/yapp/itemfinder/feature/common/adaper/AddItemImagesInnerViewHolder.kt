package com.yapp.itemfinder.feature.common.adaper

import android.net.Uri
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yapp.itemfinder.domain.model.AddItemImages
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.AddItemImagesInnerCameraViewholderItemBinding
import com.yapp.itemfinder.feature.common.databinding.AddItemImagesInnerImageViewholderItemBinding

class AddItemImagesInnerAdapter(val onCameraClicked: () -> Unit) :
    ListAdapter<String, AddItemImagesInnerViewHolders>(
        object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddItemImagesInnerViewHolders {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == CAMERA_TYPE) {
            AddItemImagesInnerViewHolders.AddItemImageInnerCameraViewHolder(
                AddItemImagesInnerCameraViewholderItemBinding.inflate(inflater, parent, false)
            )
        } else {
            AddItemImagesInnerViewHolders.AddItemImagesInnerImageViewHolder(
                AddItemImagesInnerImageViewholderItemBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (position == 0)
            CAMERA_TYPE
        else
            IMAGE_TYPE

    override fun onBindViewHolder(holder: AddItemImagesInnerViewHolders, position: Int) {
        when (holder) {
            is AddItemImagesInnerViewHolders.AddItemImageInnerCameraViewHolder -> {
                holder.itemView.setOnClickListener {
                    onCameraClicked()
                }
            }
            is AddItemImagesInnerViewHolders.AddItemImagesInnerImageViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    companion object {
        private const val CAMERA_TYPE = 0
        private const val IMAGE_TYPE = 1
    }
}

sealed class AddItemImagesInnerViewHolders(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    class AddItemImagesInnerImageViewHolder(
        val binding: AddItemImagesInnerImageViewholderItemBinding
    ) : AddItemImagesInnerViewHolders(binding) {
        private val context = binding.root.context

        fun bind(data: String) {
            Glide.with(binding.imageView)
                .load(Uri.parse(data))
                .transform(CenterCrop(),RoundedCorners(context.resources.getDimension(R.dimen.add_images_background_radius).toInt()))
                .into(binding.imageView)
        }
    }

    class AddItemImageInnerCameraViewHolder(
        val binding: AddItemImagesInnerCameraViewholderItemBinding
    ) : AddItemImagesInnerViewHolders(binding)
}
