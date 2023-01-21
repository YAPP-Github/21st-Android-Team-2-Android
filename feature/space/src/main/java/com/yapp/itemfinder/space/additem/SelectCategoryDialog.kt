package com.yapp.itemfinder.space.additem

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.yapp.itemfinder.space.databinding.SelectCategoryDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectCategoryDialog : DialogFragment() {
    private var _binding: SelectCategoryDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectCategoryDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        passSelectedCategory()
    }

    private fun passSelectedCategory() {

    }

    companion object {
        fun getInstance() = SelectCategoryDialog()
    }
}
