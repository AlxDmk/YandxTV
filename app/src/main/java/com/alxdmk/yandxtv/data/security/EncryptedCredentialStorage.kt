package com.alxdmk.yandxtv.data.security

import android.content.Context
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.alxdmk.yandxtv.domain.model.Credential
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Secure credential storage using Android Keystore + EncryptedSharedPreferences.
 * Keys never leave the device. Each siteId maps to a JSON-serialised [Credential].
 */
@Singleton
class EncryptedCredentialStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val gson = Gson()

    private val masterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val prefs by lazy {
        EncryptedSharedPreferences.create(
            context,
            "yandxtv_credentials",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun key(siteId: Long) = "cred_$siteId"

    fun saveCredential(credential: Credential) {
        val json = gson.toJson(credential)
        prefs.edit().putString(key(credential.siteId), json).apply()
    }

    fun loadCredential(siteId: Long): Credential? {
        val json = prefs.getString(key(siteId), null) ?: return null
        return runCatching { gson.fromJson(json, Credential::class.java) }.getOrNull()
    }

    fun deleteCredential(siteId: Long) {
        prefs.edit().remove(key(siteId)).apply()
    }

    fun hasCredential(siteId: Long): Boolean {
        return prefs.contains(key(siteId))
    }
}
