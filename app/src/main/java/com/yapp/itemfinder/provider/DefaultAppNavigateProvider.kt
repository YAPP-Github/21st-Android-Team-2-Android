package com.yapp.itemfinder.provider

import android.content.Context
import android.content.Intent
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.yapp.itemfinder.domain.provider.AppNavigateProvider
import com.yapp.itemfinder.prelogin.PreloginActivity
import javax.inject.Inject


class DefaultAppNavigateProvider @Inject constructor() : AppNavigateProvider {

    override fun newIntentForPrelogin(context: Context): Intent =
        PreloginActivity.newIntent(context)

    override fun newIntentForOssPlugin(context: Context): Intent {
        OssLicensesMenuActivity.setActivityTitle("오픈소스 라이센스 목록")
        return Intent(context, OssLicensesMenuActivity::class.java)
    }
}
