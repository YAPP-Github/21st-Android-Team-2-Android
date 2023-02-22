package com.yapp.itemfinder.space.managespace

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.yapp.itemfinder.space.R
import com.yapp.itemfinder.space.databinding.DeleteSpaceDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteSpaceDialog : DialogFragment() {
    private lateinit var binding: DeleteSpaceDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DeleteSpaceDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            deleteSpaceTextView.text = getString(R.string.deleteSpaceMessage).format(
                arguments?.getString(
                    SPACE_NAME
                )
            )
            confirmButton.setOnClickListener {
                setFragmentResult(
                    DELETE_SPACE_REQUEST,
                    bundleOf(SPACE_ID to arguments?.getLong(SPACE_ID))
                )
                dismiss()
            }
            cancelButton.setOnClickListener { dismiss() }
        }
    }

    companion object {
        const val DELETE_SPACE_REQUEST = "DELETE_SPACE_REQUEST"
        const val SPACE_ID = "SPACE_ID"
        const val SPACE_NAME = "SPACE_NAME"
        const val TAG = "DELETE_SPACE_DIALOG"
        fun newInstance(): DeleteSpaceDialog = DeleteSpaceDialog()
    }
}
