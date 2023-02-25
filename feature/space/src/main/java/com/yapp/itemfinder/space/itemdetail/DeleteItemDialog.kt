package com.yapp.itemfinder.space.itemdetail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.yapp.itemfinder.space.databinding.DeleteItemDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteItemDialog : DialogFragment() {
    private lateinit var binding: DeleteItemDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DeleteItemDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            confirmButton.setOnClickListener {
                setFragmentResult(
                    DELETE_ITEM_REQUEST,
                    bundleOf()
                )
                dismiss()
            }
            cancelButton.setOnClickListener { dismiss() }
        }
    }

    companion object {
        const val DELETE_ITEM_REQUEST = "DELETE_ITEM_REQUEST"
        const val TAG = "DELETE_ITEM_DIALOG_TAG"

        fun newInstance(): DeleteItemDialog = DeleteItemDialog()
    }
}
