package com.yapp.itemfinder.feature.common.datalist.adapter

import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.EditorInfo
import com.yapp.itemfinder.domain.model.AddItemName
import com.yapp.itemfinder.feature.common.databinding.AddItemNameBinding

class AddItemNameViewHolder(
    val binding: AddItemNameBinding
) : DataViewHolder<AddItemName>(binding) {
    override fun reset() {
        return
    }

    override fun bindData(data: AddItemName) {
        super.bindData(data)
        if (data.name != "") binding.itemNameEditText.setText(data.name)
    }

    override fun bindViews(data: AddItemName) {
        binding.itemNameEditText.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN || keyCode == KEYCODE_ENTER) {
                data.setItemName(binding.itemNameEditText.text.toString())
            }
            true
        }
        return
    }

}
