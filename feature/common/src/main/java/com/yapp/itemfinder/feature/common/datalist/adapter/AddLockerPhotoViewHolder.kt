package com.yapp.itemfinder.feature.common.datalist.adapter

import android.net.Uri
import android.view.RoundedCorner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.yapp.itemfinder.domain.model.AddLockerPhoto
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.AddLockerPhotoBinding
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.visible

class AddLockerPhotoViewHolder(
    val binding: AddLockerPhotoBinding
) : DataViewHolder<AddLockerPhoto>(binding) {
    private val context = binding.root.context
    override fun reset() {
        return
    }

    override fun bindData(data: AddLockerPhoto) {
        super.bindData(data)
        with(binding) {
            data.uriString?.let {
                photoIconImageView.visible()
                photoImageView.visible()

                addPhotoButton.gone()
                addPhotoDescriptionTextView.gone()
                Glide.with(photoImageView).load(Uri.parse(it))
                    .transform(
                        CenterCrop(),
                        RoundedCorners(context.resources.getDimension(R.dimen.add_locker_image_radius).toInt())
                    )
                    .into(photoImageView)
            } ?: kotlin.run {
                photoIconImageView.gone()
                photoImageView.gone()

                addPhotoButton.visible()
                addPhotoDescriptionTextView.visible()
            }
        }
    }

    override fun bindViews(data: AddLockerPhoto) {
        binding.root.setOnClickListener {
            data.uploadImage()
        }
    }
}
