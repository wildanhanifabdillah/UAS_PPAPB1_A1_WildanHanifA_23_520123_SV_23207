package com.example.quotes_uas.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref private constructor(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "UserPref"
        private const val KEY_ROLE = "role"
        private const val KEY_USERNAME = "username"

        @Volatile
        private var instance: SharedPref? = null

        // Singleton instance untuk SharedPref
        fun getInstance(context: Context): SharedPref {
            return instance ?: synchronized(this) {
                instance ?: SharedPref(context.applicationContext).also { instance = it }
            }
        }
    }

    // Menyimpan role user
    fun saveLogin(role: String) {
        sharedPref.edit().putString(KEY_ROLE, role).apply()
    }

    // Menyimpan username
    fun saveUsername(username: String) {
        sharedPref.edit().putString(KEY_USERNAME, username).apply()
    }

    // Mengambil username
    fun getUsername(): String? {
        return sharedPref.getString(KEY_USERNAME, null)
    }

    // Mengambil role user
    fun getRole(): String? {
        return sharedPref.getString(KEY_ROLE, null)
    }

    // Menghapus semua data dalam SharedPreferences
    fun clear() {
        sharedPref.edit().clear().apply()
    }
}

