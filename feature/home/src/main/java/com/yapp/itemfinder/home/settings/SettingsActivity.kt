package com.yapp.itemfinder.home.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.yapp.itemfinder.domain.provider.AppNavigateProvider
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.Depth
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.coroutines.coroutineExceptionHandler
import com.yapp.itemfinder.feature.common.extension.showShortToast
import com.yapp.itemfinder.feature.home.databinding.ActivitySettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SettingsActivity : BaseStateActivity<SettingsViewModel, ActivitySettingsBinding>(), CoroutineScope, SettingsLogoutDialog.SettingsLogoutCallback {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main.immediate + coroutineExceptionHandler

    override val vm by viewModels<SettingsViewModel>()

    override val binding by viewBinding(ActivitySettingsBinding::inflate)

    override val depth: Depth
        get() = Depth.SETTINGS

    @Inject
    lateinit var navigateProvider: AppNavigateProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun initViews() = with(binding) {
        privacyGuidanceButton.setOnClickListener {
            vm.movePrivacyGuidance()
        }
        serviceTermsOfUseButton.setOnClickListener {
            vm.moveServiceTermsOfUse()
        }
        openSourceButton.setOnClickListener {
            vm.moveOpenSource()
        }
        contactUsButton.setOnClickListener {
            vm.moveContactUs()
        }
        logoutButton.setOnClickListener {
            val dialog = SettingsLogoutDialog.newInstance()
            supportFragmentManager.let { fragmentManager ->
                dialog.show(fragmentManager, SettingsLogoutDialog.TAG)
            }
        }
        closeButton.setOnClickListener {
            finish()
        }
    }

    override fun observeData(): Job = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                vm.stateFlow.collect { state ->
                    if (state is SettingsScreenState.Success) {
                        binding.appVersionValueTextView.text = state.appVersion
                    }
                }
            }
            launch {
                vm.sideEffectFlow.collect { sideEffect ->
                    when (sideEffect) {
                        is SettingsScreenSideEffect.Start -> handleStart(sideEffect = sideEffect)
                        is SettingsScreenSideEffect.ShowLoading -> {
                            binding.loadingContainer.isVisible = sideEffect.isShow
                        }
                        is SettingsScreenSideEffect.ShowErrorToast -> {
                            if (sideEffect.message.isNullOrEmpty().not()) {
                                showShortToast(requireNotNull(sideEffect.message))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleStart(sideEffect: SettingsScreenSideEffect.Start) {
        when(sideEffect) {
            is SettingsScreenSideEffect.Start.PrivacyHandle -> {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(sideEffect.url))
                )
            }
            is SettingsScreenSideEffect.Start.TermsOfUse -> {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(sideEffect.url))
                )
            }
            is SettingsScreenSideEffect.Start.OpenSource -> {
                startActivity(navigateProvider.newIntentForOssPlugin(context = this))
            }
            is SettingsScreenSideEffect.Start.ContactUs -> {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "*/*"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(sideEffect.email)) // 받는 사람 이메일
                    putExtra(Intent.EXTRA_SUBJECT, sideEffect.subject) // 메일 제목
                    putExtra(Intent.EXTRA_TEXT, sideEffect.text) // 메일 내용
                }

                startActivity(intent)
            }
            is SettingsScreenSideEffect.Start.ClearAndGoToPreLogin -> {
                startActivity(
                    navigateProvider.newIntentForPrelogin(this).apply {
                        addFlags(FLAG_ACTIVITY_CLEAR_TOP)
                    }
                )
            }
        }
    }

    override fun logout() {
        vm.logout()
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, SettingsActivity::class.java)

    }

}
