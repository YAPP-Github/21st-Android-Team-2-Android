package com.yapp.itemfinder.space.managespace

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.yapp.itemfinder.space.databinding.AddSpaceDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSpaceDialog : DialogFragment() {
    private var _binding: AddSpaceDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddSpaceDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            addNewSpaceButton.setOnClickListener {
                val name = spaceNameEditText.text.toString()
                passName(name)
            }
            cancelButton.setOnClickListener { dismiss() }
        }
    }

    private fun passName(name: String) {
        val intent = Intent()
        intent.putExtra("name", name)
        targetFragment?.onActivityResult(
            targetRequestCode,
            ManageSpaceFragment.ADD_SPACE_REQUEST_CODE,
            intent
        )
        dismiss()
    }

    fun getInstance(): AddSpaceDialog {
        return AddSpaceDialog()
    }
}
