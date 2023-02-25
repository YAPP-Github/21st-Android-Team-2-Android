package com.yapp.itemfinder.home.settings

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.yapp.itemfinder.feature.home.R
import com.yapp.itemfinder.feature.home.databinding.DialogSettingsLogoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsLogoutDialog : DialogFragment() {

    interface SettingsLogoutCallback {

        fun logout()

    }

    private lateinit var binding: DialogSettingsLogoutBinding

    private lateinit var settingsLogoutCallback: SettingsLogoutCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SettingsLogoutCallback) {
            settingsLogoutCallback = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSettingsLogoutBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            logoutMessage.text = getString(R.string.do_you_want_logout)
            confirmButton.setOnClickListener {
                settingsLogoutCallback.logout()
                dismiss()
            }
            cancelButton.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "SettingsLogoutDialog"

        fun newInstance(): SettingsLogoutDialog = SettingsLogoutDialog()
    }
}
