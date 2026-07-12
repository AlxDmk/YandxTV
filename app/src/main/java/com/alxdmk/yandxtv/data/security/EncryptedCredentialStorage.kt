package com.alxdmk.yandxtv.data.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.alxdmk.yandxtv.domain.model.Credential
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Secure credential storage using Android Keystore + EncryptedSharedPreferences.
 * Keys never leave the device. Credentials are AES-256-GCM encrypted at rest.
 */
@Singleton
class EncryptedCredentialStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            PREFS_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveCredential(credential: Credential) {
        with(sharedPreferences.edit()) {
            putString(usernameKey(credential.siteId), credential.username)
            putString(passwordKey(credential.siteId), credential.password)
            putString(noteKey(credential.siteId), credential.note)
            apply()
        }
    }

    fun getCredential(siteId: Long): Credential? {
        val username = sharedPreferences.getString(usernameKey(siteId), null) ?: return null
        return Credential(
            siteId = siteId,
            username = username,
            password = sharedPreferences.getString(passwordKey(siteId), "") ?: "",
            note = sharedPreferences.getString(noteKey(siteId), "") ?: ""
        )
    }

    fun deleteCredential(siteId: Long) {
        with(sharedPreferences.edit()) {
            remove(usernameKey(siteId))
            remove(passwordKey(siteId))
            remove(noteKey(siteId))
            apply()
        }
    }

    fun hasCredential(siteId: Long): Boolean =
        sharedPreferences.contains(usernameKey(siteId))

    private fun usernameKey(siteId: Long) = "cred_${siteId}_username"
    private fun passwordKey(siteId: Long) = "cred_${siteId}_password"
    private fun noteKey(siteId: Long) = "cred_${siteId}_note"

    companion object {
        private const val PREFS_FILE_NAME = "yandxtv_credentials"
    }
}
