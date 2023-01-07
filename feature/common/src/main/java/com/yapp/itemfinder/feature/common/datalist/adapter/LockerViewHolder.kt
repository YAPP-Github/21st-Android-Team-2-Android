package com.yapp.itemfinder.feature.common.datalist.adapter

import android.os.Build
import android.view.Gravity
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import com.yapp.itemfinder.domain.model.Locker
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.LockerItemBinding

class LockerViewHolder(
    val binding: LockerItemBinding
) : DataViewHolder<Locker>(binding) {
    override fun reset() {
    }

    override fun bindData(data: Locker) {
        super.bindData(data)
        binding.lockerItemTextView.text = data.name
        binding.lockerItemImageView.setImageResource(data.icon)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun bindViews(data: Locker) {
        binding.spinnerImageButton.setOnClickListener {
            val popupMenu = PopupMenu(itemView.context, binding.spinnerImageButton, Gravity.END, 0, R.style.PopupMenu)
            popupMenu.inflate(R.menu.pop_up_menu)
            popupMenu.setForceShowIcon(true)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.edit_menu -> {
                        data.editLocker()
                        true
                    }
                    R.id.delete_menu -> {
                        data.deleteLocker()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }
}
