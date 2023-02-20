package com.yapp.itemfinder.feature.common.extension

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.onDone(callback: (String) -> Unit) {
    // These lines optional if you don't want to set in Xml
    imeOptions = EditorInfo.IME_ACTION_DONE
    maxLines = 1
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke(text.toString())
            true
        }
        false
    }
}
