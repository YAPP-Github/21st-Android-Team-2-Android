package com.yapp.itemfinder.provider

import android.os.Build
import com.yapp.itemfinder.BuildConfig
import com.yapp.itemfinder.domain.entity.version.Version
import com.yapp.itemfinder.domain.provider.BuildConfigProvider
import javax.inject.Inject

class DefaultBuildConfigProvider @Inject constructor(): BuildConfigProvider {

    override val applicationId: String
        get() = BuildConfig.APPLICATION_ID

    override val versionName: String
        get() = BuildConfig.VERSION_NAME

    override val version: Version
        get() = Version.from(BuildConfig.VERSION_NAME)


    override val buildType: String
        get() = if (BuildConfig.DEBUG) BuildConfig.BUILD_DEBUG else BuildConfig.BUILD_RELEASE

    override val isProd: Boolean
        get() = BuildConfig.FLAVOR == "prod"

    override val isBuildTypeDebug: Boolean
        get() = BuildConfig.DEBUG

    override val isBuildTypeRelease: Boolean
        get() = !BuildConfig.DEBUG

    override val deviceModel: String
        get() = Build.MODEL

    override val deviceOs: String
        get() = Build.VERSION.RELEASE

}
