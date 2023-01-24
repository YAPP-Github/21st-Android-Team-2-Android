package com.yapp.itemfinder.feature.common.views

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.yapp.itemfinder.feature.common.R
import com.yapp.itemfinder.feature.common.databinding.SnackbarBinding

class SnackBarView(
    val view: View,
    private val message: String
) {

    private val context = view.context
    private val snackBar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

    private val snackBarBinding: SnackbarBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.snackbar, null, false)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackBarLayout) {
            removeAllViews()
            setPadding(20, 20, 20, 88)
            setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
            addView(snackBarBinding.root, 0)
        }
    }

    private fun initData() {
        snackBarBinding.snackbarTextView.text = message
    }

    fun show() {
        snackBar.show()
    }

    companion object {
        fun make(view: View, message: String) = SnackBarView(view, message)
    }
}
