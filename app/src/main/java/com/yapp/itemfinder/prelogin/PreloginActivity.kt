package com.yapp.itemfinder.prelogin

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.yapp.itemfinder.databinding.AcitivityPreloginBinding
import com.yapp.itemfinder.domain.entity.signup.SignUpEntity
import com.yapp.itemfinder.domain.type.GenderType
import com.yapp.itemfinder.domain.type.SocialType
import com.yapp.itemfinder.feature.common.BaseStateActivity
import com.yapp.itemfinder.feature.common.binding.viewBinding
import com.yapp.itemfinder.feature.common.extension.gone
import com.yapp.itemfinder.feature.common.extension.visible
import com.yapp.itemfinder.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreloginActivity : BaseStateActivity<PreloginViewModel, AcitivityPreloginBinding>() {

    override val vm by viewModels<PreloginViewModel>()

    override val binding by viewBinding(AcitivityPreloginBinding::inflate)

    override fun initViews() = with(binding) {
        kakaoLoginButton.setOnClickListener {
            vm.requestKakaoLogin()
        }
    }

    override fun observeData(): Job {
        val job = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.stateFlow.collect { state ->
                        when (state) {
                            PreloginState.Uninitialized -> handleUninitialized()
                            PreloginState.Loading -> handleLoading()
                            PreloginState.Success -> handleSuccess()
                            PreloginState.Error -> handleError()
                        }
                    }
                }
                launch {
                    vm.sideEffectFlow.collect { sideEffect ->
                        when (sideEffect) {
                            is PreloginSideEffect.RequestKakaoLogin -> handleRequestKakaoLogin()
                            is PreloginSideEffect.ShowToast -> {
                                Toast.makeText(this@PreloginActivity, sideEffect.message, Toast.LENGTH_SHORT).show()
                            }
                            is PreloginSideEffect.StartHome -> {
                                startActivity(HomeActivity.newIntent(this@PreloginActivity))
                                finish()
                            }
                        }
                    }
                }
            }
        }
        return job
    }

    private fun handleUninitialized() = with(binding) {
        kakaoLoginButton.isClickable = true
        loadingContainer.gone()
    }

    private fun handleLoading() = with(binding) {
        kakaoLoginButton.isClickable = false
        loadingContainer.visible()
    }

    private fun handleSuccess() = with(binding) {
        kakaoLoginButton.isClickable = false
        loadingContainer.gone()
    }

    private fun handleError() = with(binding) {
        kakaoLoginButton.isClickable = true
        loadingContainer.gone()
    }

    private fun handleRequestKakaoLogin() {
        val kakaoAccountFallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "????????????????????? ????????? ??????", error)
                handleUninitialized()
            } else if (token != null) {
                requestSignUp(token)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            // ?????????????????? ?????????
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("PreloginActivity", "????????? ??????", error)

                    // ???????????? ???????????? ?????? ??? ???????????? ?????? ?????? ???????????? ???????????? ????????? ??????,
                    // ???????????? ????????? ????????? ?????? ????????????????????? ????????? ?????? ?????? ????????? ????????? ?????? (???: ?????? ??????)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        handleUninitialized()
                        return@loginWithKakaoTalk
                    }

                    // ??????????????? ????????? ?????????????????? ?????? ??????, ????????????????????? ????????? ??????
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoAccountFallback)
                } else if (token != null) {
                    requestSignUp(token)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoAccountFallback)
        }
    }

    private fun requestSignUp(token: OAuthToken) {
        getKakaoUserInfo { kakaoUser ->
            kakaoUser.id ?: return@getKakaoUserInfo
            val kakaoAccount = kakaoUser.kakaoAccount
            vm.requestSignIn(
                SignUpEntity(
                    socialId = kakaoUser.id.toString(),
                    socialType = SocialType.KAKAO,
                    nickname = kakaoAccount?.profile?.nickname ?: "",
                    email = kakaoAccount?.email,
                    gender = GenderType.fromKakaoGender(kakaoAccount?.gender?.name),
                    birthYear = kakaoAccount?.birthyear?.toInt()
                )
            )
            Log.i("PreloginActivity", "kakao ????????? ?????? ${token.accessToken}, account : $kakaoAccount")
        }
    }

    private fun getKakaoUserInfo(accessUser: (User) -> Unit) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "????????? ?????? ?????? ??????", error)
            } else if (user != null) {
                val kakaoAccount = user.kakaoAccount
                val scopes = mutableListOf<String>().apply {
                    add("openid")
                    if (kakaoAccount?.emailNeedsAgreement == true) {
                        add("account_email")
                    }
                    if (kakaoAccount?.birthdayNeedsAgreement == true) {
                        add("birthday")
                    }
                    if (kakaoAccount?.birthyearNeedsAgreement == true) {
                        add("birthyear")
                    }
                    if (kakaoAccount?.genderNeedsAgreement == true) {
                        add("gender")
                    }
                    if (kakaoAccount?.phoneNumberNeedsAgreement == true) {
                        add("phone_number")
                    }
                    if (kakaoAccount?.profileNeedsAgreement == true) {
                        add("profile")
                    }
                    if (kakaoAccount?.ageRangeNeedsAgreement == true) {
                        add("age_range")
                    }
                    if (kakaoAccount?.ciNeedsAgreement == true) {
                        add("account_ci")
                    }
                }

                if (scopes.isNotEmpty()) {
                    Log.d(TAG, "??????????????? ?????? ????????? ????????? ?????????.")

                    // OpenID Connect ?????? ???
                    // scope ????????? "openid" ???????????? ???????????? ???????????? ???
                    // ?????? ???????????? ???????????? ?????? ??????, ID ????????? ??????????????? ??????
                    // add("openid")

                    //scope ????????? ???????????? ????????? ????????? ??????
                    UserApiClient.instance.loginWithNewScopes(this, scopes) { token, error ->
                        if (error != null) {
                            Log.e(TAG, "????????? ?????? ?????? ??????", error)
                        } else {
                            Log.d(TAG, "allowed scopes: ${token!!.scopes}")

                            // ????????? ?????? ?????????
                            UserApiClient.instance.me { user, error ->
                                if (error != null) {
                                    Log.e(TAG, "????????? ?????? ?????? ??????", error)
                                } else if (user != null) {
                                    Log.i(TAG, "????????? ?????? ?????? ??????")
                                    accessUser(user)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {

        const val TAG = "PreloginActivity"

        fun newIntent(context: Context) = Intent(context, PreloginActivity::class.java)

    }


}
