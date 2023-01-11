package com.yapp.itemfinder.space.managespace

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.yapp.itemfinder.space.databinding.AddSpaceDialogBinding

class AddSpaceDialog: DialogFragment() {
    private var _binding: AddSpaceDialogBinding?=null
    private val binding get() = _binding!!

    private val vm by viewModels<ManageSpaceViewModel>()

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

        }
    }

    private fun addNewSpace(name: String){
        vm.addItem(name)
        dismiss()
    }

    fun getInstance(): AddSpaceDialog{
        return AddSpaceDialog()
    }
}
