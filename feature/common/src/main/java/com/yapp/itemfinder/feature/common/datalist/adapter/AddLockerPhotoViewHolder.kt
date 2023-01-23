package com.yapp.itemfinder.feature.common.datalist.adapter

import android.net.Uri
import com.bumptech.glide.Glide
import com.yapp.itemfinder.domain.model.AddLockerPhoto
import com.yapp.itemfinder.feature.common.databinding.AddLockerPhotoBinding
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.visible

class AddLockerPhotoViewHolder(
    val binding: AddLockerPhotoBinding
) : DataViewHolder<AddLockerPhoto>(binding) {
    override fun reset() {
        return
    }

    override fun bindData(data: AddLockerPhoto) {
        super.bindData(data)
        with(binding) {
            data.uriString?.let {
                addPhotoButton.gone()
                addPhotoDescriptionTextView.gone()
                photoImageView.visible()
                Glide.with(photoImageView).load(Uri.parse(it))
                    .override(photoImageView.height, photoImageView.width)
                    .centerCrop()
                    .into(photoImageView)
            }
        }
    }

    override fun bindViews(data: AddLockerPhoto) {
        binding.root.setOnClickListener {
            data.uploadImage()
        }
    }
}
