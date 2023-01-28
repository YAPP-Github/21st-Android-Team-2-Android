package com.yapp.itemfinder.feature.common.adaper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
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
        if (holder is AddItemImagesInnerViewHolders.AddItemImageInnerCameraViewHolder){
            holder.itemView.setOnClickListener {
                onCameraClicked()
            }
        }


    }

    companion object {
        private const val CAMERA_TYPE = 0
        private const val IMAGE_TYPE = 1
    }


}

sealed class AddItemImagesInnerViewHolders(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
    class AddItemImagesInnerImageViewHolder(
        binding: AddItemImagesInnerImageViewholderItemBinding
    ) : AddItemImagesInnerViewHolders(binding)

    class AddItemImageInnerCameraViewHolder(
        binding: AddItemImagesInnerCameraViewholderItemBinding
    ) :  AddItemImagesInnerViewHolders(binding)

}
