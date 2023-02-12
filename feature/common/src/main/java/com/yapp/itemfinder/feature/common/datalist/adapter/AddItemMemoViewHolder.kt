package com.yapp.itemfinder.feature.common.datalist.adapter

import android.text.InputType
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.yapp.itemfinder.domain.model.AddItemMemo
import com.yapp.itemfinder.domain.model.ScreenMode
import com.yapp.itemfinder.feature.common.databinding.AddItemMemoBinding

class AddItemMemoViewHolder(
    val binding: AddItemMemoBinding
) : DataViewHolder<AddItemMemo>(binding) {
    override fun reset() {
        return
    }

    override fun bindData(data: AddItemMemo) {
        super.bindData(data)
        if (data.mode == ScreenMode.EDIT_MODE) binding.memoEditText.setText(data.memo)
        data.saveHandler = {
            data.setItemMemo(binding.memoEditText.text.toString())
        }
    }

    override fun bindViews(data: AddItemMemo) {
        binding.memoEditText.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.memoEditText.setRawInputType(InputType.TYPE_CLASS_TEXT)
        binding.memoEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                data.setItemMemo(textView.text.toString())
            }
            false
        }
        binding.memoEditText.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                data.setItemMemo(binding.memoEditText.text.toString())
            }
            false
        }
        return
    }
}
