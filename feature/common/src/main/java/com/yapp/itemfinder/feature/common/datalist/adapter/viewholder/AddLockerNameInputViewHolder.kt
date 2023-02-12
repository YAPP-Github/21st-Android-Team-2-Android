package com.yapp.itemfinder.feature.common.datalist.adapter.viewholder

import android.view.inputmethod.EditorInfo
import com.yapp.itemfinder.domain.model.AddLockerNameInput
import com.yapp.itemfinder.feature.common.databinding.AddLockerNameInputBinding
import com.yapp.itemfinder.feature.common.datalist.adapter.DataViewHolder

class AddLockerNameInputViewHolder(
    val binding: AddLockerNameInputBinding
) : DataViewHolder<AddLockerNameInput>(binding) {
    override fun reset() {
        return
    }

    override fun bindData(data: AddLockerNameInput) {
        super.bindData(data)
        data.saveHandler = {
            data.enterName(binding.lockerNameEditText.text.toString())
        }
    }

    override fun bindViews(data: AddLockerNameInput) {
        binding.lockerNameEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                data.enterName(textView.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }
}
