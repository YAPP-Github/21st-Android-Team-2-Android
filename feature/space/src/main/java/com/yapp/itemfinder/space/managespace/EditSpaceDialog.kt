package com.yapp.itemfinder.space.managespace

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.yapp.itemfinder.space.databinding.AddSpaceDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditSpaceDialog : DialogFragment() {
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
            titleTextView.text = "공간 수정"
            confirmButton.text = "수정"
            spaceNameEditText.setText(requireArguments().getString(SPACE_NAME))
            confirmButton.setOnClickListener {
                val name = spaceNameEditText.text.toString()
                passNewSpace(name, requireArguments().getLong(SPACE_ID))
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

    private fun passNewSpace(name: String, id: Long) {
        val bundle = Bundle().apply {
            putLong(SPACE_ID_KEY, id)
            putString(NEW_SPACE_NAME_KEY, name)
        }
        setFragmentResult(
            EDIT_NAME_REQUEST_KEY,
            bundle
        )
        dismiss()
    }

    companion object {
        const val EDIT_NAME_REQUEST_KEY = "EDIT_NAME_REQUEST_KEY"
        const val NEW_SPACE_NAME_KEY = "NEW_SPACE_NAME_KEY"
        const val SPACE_ID_KEY = "NEW_SPACE_ID_KEY"
        const val TAG = "EDIT_SPACE_DIALOG"
        const val SPACE_ID = "SPACE_ID"
        const val SPACE_NAME = "SPACE_NAME"
        fun newInstance(): EditSpaceDialog = EditSpaceDialog()
    }

}
