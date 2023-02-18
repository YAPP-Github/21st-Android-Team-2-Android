package com.yapp.itemfinder.space.managespace

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.yapp.itemfinder.domain.model.Tag
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
    ): View {
        _binding = AddSpaceDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            confirmButton.setOnClickListener {
                val name = spaceNameEditText.text.toString()
                passName(name)
            }
            cancelButton.setOnClickListener { dismiss() }

            spaceNameEditText.addTextChangedListener(object : TextWatcher {

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null) {
                        if ((p0.length) > 9) {
                            spaceNameEditText.setText(spaceNameEditText.text.substring(0, 9))
                            spaceNameEditText.setSelection(spaceNameEditText.length())
                        }
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

                override fun afterTextChanged(p0: Editable?) = Unit
            })
        }
    }

    private fun passName(name: String) {
        setFragmentResult(
            NEW_SPACE_REQUEST_KEY,
            bundleOf(NEW_SPACE_NAME_BUNDLE_KEY to name)
        )
        dismiss()
    }

    companion object {
        const val NEW_SPACE_REQUEST_KEY = "new space request"
        const val NEW_SPACE_NAME_BUNDLE_KEY = "new space name"
        const val TAG = "add space dialog"
        fun newInstance(): AddSpaceDialog = AddSpaceDialog()
    }

}
