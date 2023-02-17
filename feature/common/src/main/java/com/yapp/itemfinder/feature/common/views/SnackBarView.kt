package com.yapp.itemfinder.feature.common.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.SnackbarBinding

class SnackBarView(
    viewGroup: ViewGroup,
    message: String
) {

    private val context = viewGroup.context
    private val snackBar = Snackbar.make(viewGroup, "", Snackbar.LENGTH_SHORT)
    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

    private val snackBarBinding: SnackbarBinding =
        SnackbarBinding.inflate(LayoutInflater.from(context), viewGroup, false)

    init {
        with(snackBarLayout) {
            removeAllViews()
            setPadding(20, 20, 20, 88)
            setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
            addView(snackBarBinding.root, 0)
        }
        snackBarBinding.snackbarTextView.text = message
    }

    fun show() {
        snackBar.show()
    }

    companion object {
        fun make(view: ViewGroup, message: String) = SnackBarView(view, message)
    }
}
