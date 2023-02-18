package com.yapp.itemfinder.space.addlocker

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.yapp.itemfinder.space.databinding.ChangeLockerImageDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeLockerImageDialog : DialogFragment() {
    private var _binding: ChangeLockerImageDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChangeLockerImageDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            confirmButton.setOnClickListener {
                setFragmentResult(CONFIRM_KEY, bundleOf())
                dismiss()
            }
            cancelButton.setOnClickListener { dismiss() }
        }
    }

    companion object {
        const val CONFIRM_KEY = "CONFIRM_KEY"
        fun newInstance(): ChangeLockerImageDialog = ChangeLockerImageDialog()
    }
}
