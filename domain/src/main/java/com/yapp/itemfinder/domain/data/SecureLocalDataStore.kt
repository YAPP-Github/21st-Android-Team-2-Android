package com.yapp.itemfinder.domain.data

interface SecureLocalDataStore {
    fun <T> put(pref: SecureLocalData<T>, value: T)
    fun <T> get(pref: SecureLocalData<T>): T
    fun clear(prefName: PrefName)
}
