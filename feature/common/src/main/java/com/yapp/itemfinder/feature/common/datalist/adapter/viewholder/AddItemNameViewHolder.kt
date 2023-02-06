package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.EditorInfo
import com.yapp.itemfinder.domain.model.AddItemName
import com.yapp.itemfinder.domain.model.ScreenMode
import com.yapp.itemfinder.feature.common.databinding.AddItemNameBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddItemNameViewHolder(
    val binding: AddItemNameBinding
) : DataViewHolder<AddItemName>(binding) {
    override fun reset() {
        return
    }

    override fun bindData(data: AddItemName) {
        super.bindData(data)
        if (data.mode == ScreenMode.EDIT_MODE) binding.itemNameEditText.setText(data.name)
        data.saveHandler = {
            data.setItemName(binding.itemNameEditText.text.toString())
        }
    }

    override fun bindViews(data: AddItemName) {
        binding.itemNameEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                data.setItemName(textView.text.toString())
            }
            false
        }
        binding.itemNameEditText.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                data.setItemName(binding.itemNameEditText.text.toString())
            }
            false
        }
        return
    }

}
