package com.yapp.itemfinder.feature.common.datalist.adapter

import android.os.Build
import android.view.Gravity
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import com.yapp.itemfinder.domain.model.ManageSpaceItem
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.ManageSpaceItemBinding

class ManageSpaceViewHolder(
    val binding: ManageSpaceItemBinding
) : DataViewHolder<ManageSpaceItem>(binding){
    override fun reset() {
        return
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun bindViews(data: ManageSpaceItem) {
        binding.spaceName.text = data.name
        binding.spinnerImageButton.setOnClickListener {
            val popupMenu = PopupMenu(itemView.context, binding.spinnerImageButton, Gravity.END, 0, R.style.PopupMenu)
            popupMenu.inflate(R.menu.pop_up_menu)
            popupMenu.setForceShowIcon(true)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.edit_menu -> {
                        data.editSpaceDialog()
                        true
                    }
                    R.id.delete_menu -> {
                        data.deleteSpaceDialog()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }
}
