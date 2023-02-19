package com.yapp.itemfinder.space.lockerlist

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
import com.yapp.itemfinder.space.databinding.DeleteLockerDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteLockerDialog : DialogFragment() {
    private lateinit var binding: DeleteLockerDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DeleteLockerDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            deleteLockerMessage.text = getString(R.string.deleteLockerMessage).format(
                arguments?.getString(
                    DELETE_LOCKER_NAME
                )
            )
            confirmButton.setOnClickListener {
                setFragmentResult(
                    CONFIRM_KEY,
                    bundleOf(DELETE_LOCKER_ID to arguments?.getLong(DELETE_LOCKER_ID))
                )
                dismiss()
            }
            cancelButton.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        const val CONFIRM_KEY = "CONFIRM_KEY"
        const val DELETE_LOCKER_ID = "DELETE_LOCKER_ID"
        const val DELETE_LOCKER_NAME = "DELETE_LOCKER_NAME"
        fun newInstance(): DeleteLockerDialog = DeleteLockerDialog()
    }
}
