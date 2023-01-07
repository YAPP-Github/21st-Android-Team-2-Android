package com.yapp.itemfinder.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.google.gson.Gson
import com.yapp.itemfinder.domain.data.PrefName
import com.yapp.itemfinder.domain.data.SecureLocalData
import com.yapp.itemfinder.domain.data.SecureLocalDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultSecureLocalDataStore @Inject constructor(
    @ApplicationContext
    val context: Context,
) : SecureLocalDataStore {

    private val preferencesMap = mutableMapOf<String, SharedPreferences>()

    private fun getSharedPreferences(prefName: PrefName): SharedPreferences {
        return preferencesMap[prefName.value] ?: synchronized(this) {
             EncryptedSharedPreferences.create(
                prefName.value,
                prefName.alias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ).apply {
                preferencesMap[prefName.value] = this
            }
        }
    }

    private fun getInt(prefName: PrefName, key: String, defaultValue: Int): Int {
        return getSharedPreferences(prefName).getInt(key, defaultValue)
    }

    private fun getLong(prefName: PrefName, key: String, defaultValue: Long): Long {
        return getSharedPreferences(prefName).getLong(key, defaultValue)
    }

    private fun getFloat(prefName: PrefName, key: String, defaultValue: Float): Float {
        return getSharedPreferences(prefName).getFloat(key, defaultValue)
    }

    private fun getBoolean(prefName: PrefName, key: String, defaultValue: Boolean): Boolean {
        return getSharedPreferences(prefName).getBoolean(key, defaultValue)
    }

    private fun getString(prefName: PrefName, key: String, defaultValue: String): String {
        return getSharedPreferences(prefName).getString(key, defaultValue) ?: defaultValue
    }

    private fun getStringSet(prefName: PrefName, key: String, defaultValue: Set<String>): Set<String> {
        return getSharedPreferences(prefName).getStringSet(key, defaultValue) ?: defaultValue
    }

    override fun <T> put(pref: SecureLocalData<T>, value: T) {
        with(getSharedPreferences(pref.prefName)) {
            if (value == null) {
                edit().remove(pref.key).apply()
                return
            }

            edit().run {
                when (value) {
                    is Int -> putInt(pref.key, value)
                    is String -> putString(pref.key, value)
                    is Boolean -> putBoolean(pref.key, value)
                    is Float -> putFloat(pref.key, value)
                    is Long -> putLong(pref.key, value)
                    is Set<*> -> putStringSet(pref.key, value as Set<String>)
                    else -> {
                        // primitive type이 아닌 경우, stringify json으로 저장.
                        putString(pref.key, Gson().toJson(value))
                    }
                }.apply()
            }

        }


    }

    override fun <T> get(data: SecureLocalData<T>): T {
        return when (data.default) {
            is Int -> getInt(data.prefName, data.key, data.default as Int) as T
            is Long -> getLong(data.prefName, data.key, data.default as Long) as T
            is Float -> getFloat(data.prefName, data.key, data.default as Float) as T
            is Boolean -> getBoolean(data.prefName, data.key, data.default as Boolean) as T
            is String -> getString(data.prefName, data.key, data.default as String) as T
            is Set<*> -> getStringSet(data.prefName, data.key, data.default as Set<String>) as T
            // primitive type이 아닌 경우, stringify json으로 저장.
            else -> {
                val jsonString = getString(data.prefName, data.key, "")
                if (jsonString.isEmpty()) data.default
                else Gson().fromJson(jsonString, data.complexType)
            }

        }
    }

    override fun clear(prefName: PrefName) {
        getSharedPreferences(prefName).edit().clear().apply()
    }

}
