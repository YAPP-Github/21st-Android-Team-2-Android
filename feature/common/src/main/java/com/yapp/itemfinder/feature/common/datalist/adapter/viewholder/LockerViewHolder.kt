package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import android.os.Build
import android.view.Gravity
import android.widget.PopupMenu
import com.yapp.itemfinder.domain.model.LockerEntity
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.LockerListItemBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder
import com.yapp.itemfinder.feature.common.extension.toDrawable

class LockerViewHolder(
    val binding: LockerListItemBinding
) : DataViewHolder<LockerEntity>(binding) {
    override fun reset() {
    }

    override fun bindData(data: LockerEntity) {
        super.bindData(data)
        binding.lockerItemTextView.text = data.name
        binding.lockerItemImageView.setImageDrawable(data.icon.toDrawable(binding.root.context))
    }

    override fun bindViews(data: LockerEntity) {
        binding.spinnerImageButton.setOnClickListener {
            val popupMenu = PopupMenu(
                itemView.context,
                binding.spinnerImageButton,
                Gravity.END,
                0,
                R.style.PopupMenu
            )
            popupMenu.inflate(R.menu.pop_up_menu)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popupMenu.setForceShowIcon(true)
            }
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit_menu -> {
                        data.editLocker()
                        true
                    }
                    R.id.delete_menu -> {
                        data.openDeleteDialog()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
        binding.root.setOnClickListener {
            data.moveLockerDetail()
        }
    }
}
