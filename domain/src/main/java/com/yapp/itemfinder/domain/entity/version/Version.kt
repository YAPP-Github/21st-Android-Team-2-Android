package com.yapp.itemfinder.domain.entity.version


data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
) {
    companion object {
        private val regex = "(\\d+)(?:\\.(?:(\\d+)\\.)?(\\d+))?".toRegex()

        fun from(versionName: String): Version =
            if (versionName.isBlank()) {
                Version(0, 0, 0)
            } else {
                val versionNumbers = regex.find(versionName)!!.groupValues.drop(1)
                Version(
                    major = parseInt(versionNumbers.getOrNull(0)),
                    minor = parseInt(versionNumbers.getOrNull(1)),
                    patch = parseInt(versionNumbers.getOrNull(2))
                )
            }

        private fun parseInt(text: String?, defaultValue: Int = 0): Int =
            if (text.isNullOrBlank()) {
                defaultValue
            } else {
                try {
                    Integer.parseInt(text)
                } catch (ignored: NumberFormatException) {
                    defaultValue
                }
            }
    }

    fun updateRequired(minimumVersion: Version): Boolean {
        val versions = listOf(
            minimumVersion.major to major,
            minimumVersion.minor to minor,
            minimumVersion.patch to patch
        )
        for ((first, second) in versions) {
            if (first > second) {
                return true
            } else if (first < second) {
                return false
            }
        }
        return false
    }
}
