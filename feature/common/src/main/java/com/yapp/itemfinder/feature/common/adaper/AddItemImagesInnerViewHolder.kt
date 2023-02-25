package com.yapp.itemfinder.feature.common.adaper

import android.graphics.Color
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
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

class AddItemImagesInnerAdapter(val onCameraClicked: () -> Unit, val onCancelClicked: (Int) -> Unit) :
    ListAdapter<AddItemImagesInnerData, AddItemImagesInnerViewHolders>(
        object : DiffUtil.ItemCallback<AddItemImagesInnerData>() {
            override fun areItemsTheSame(
                oldItem: AddItemImagesInnerData,
                newItem: AddItemImagesInnerData
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: AddItemImagesInnerData,
                newItem: AddItemImagesInnerData
            ): Boolean {
                if (oldItem is AddItemImagesInnerData.AddItemImagesInnerImageData
                    && newItem is AddItemImagesInnerData.AddItemImagesInnerImageData
                ) {
                    return oldItem.uriString == newItem.uriString
                }

                return oldItem == newItem
            }
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
                holder.bind(getItem(position) as AddItemImagesInnerData.AddItemImagesInnerCameraData)
            }
            is AddItemImagesInnerViewHolders.AddItemImagesInnerImageViewHolder -> {
                val item = getItem(position) as AddItemImagesInnerData.AddItemImagesInnerImageData
                holder.bind(item) {

                    onCancelClicked(holder.adapterPosition)

//                    val newList = currentList.toMutableList().apply {
//                        removeAt(holder.adapterPosition)
//                    }
//                    submitList(newList)
                }
            }
        }
    }

    companion object {
        private const val CAMERA_TYPE = 0
        private const val IMAGE_TYPE = 1
    }
}

sealed class AddItemImagesInnerData {
    data class AddItemImagesInnerCameraData(val currentCount: Int, val maxCount: Int) : AddItemImagesInnerData()
    data class AddItemImagesInnerImageData(val uriString: String) : AddItemImagesInnerData()
}

sealed class AddItemImagesInnerViewHolders(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    class AddItemImagesInnerImageViewHolder(
        val binding: AddItemImagesInnerImageViewholderItemBinding
    ) : AddItemImagesInnerViewHolders(binding) {
        private val context = binding.root.context

        fun bind(data: AddItemImagesInnerData.AddItemImagesInnerImageData, removeHandler: () -> Unit) {
            Glide.with(binding.imageView)
//                .load(Uri.parse(data.uriString))
                .load(data.uriString)
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        context.resources.getDimension(R.dimen.add_images_background_radius).toInt()
                    )
                )
                .into(binding.imageView)

            binding.cancelImageView.setOnClickListener {
                removeHandler()
            }
        }

    }

    class AddItemImageInnerCameraViewHolder(
        val binding: AddItemImagesInnerCameraViewholderItemBinding
    ) : AddItemImagesInnerViewHolders(binding) {
        val context = binding.root.context
        fun bind(data: AddItemImagesInnerData.AddItemImagesInnerCameraData){
            val text = "${data.currentCount} / ${data.maxCount}"
            val spannableString = SpannableString(text)
            spannableString.setSpan(
                ForegroundColorSpan(context.resources.getColor(R.color.orange))
                , 0
                , text.indexOfFirst { it == '/' }
                , Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            binding.imageCount.text =spannableString
        }
    }
}
