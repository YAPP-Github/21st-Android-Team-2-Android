package com.yapp.itemfinder.domain.provider

import com.yapp.itemfinder.domain.entity.version.Version

interface BuildConfigProvider {

    /**
     * Application ID
     */
    val applicationId: String

    /**
     * Application Version Name
     */
    val versionName: String

    /**
     * Application [Version] from [versionName]
     */
    val version: Version

    /**
     * Application Build Type
     *
     * One of following values:
     * - debug
     * - release
     *
     * See constants.gradle for details.
     */
    val buildType: String

    val isProd: Boolean

    /**
     * Flag for Debug BuildType
     *
     * Never negate this value!
     */
    val isBuildTypeDebug: Boolean

    /**
     * Flag for Debug BuildType
     *
     * Never negate this value!
     */
    val isBuildTypeRelease: Boolean

    /**
     * Device [deviceModel]
     */
    val deviceModel: String

    /**
     * Device [deviceOs]
     */
    val deviceOs: String

}
