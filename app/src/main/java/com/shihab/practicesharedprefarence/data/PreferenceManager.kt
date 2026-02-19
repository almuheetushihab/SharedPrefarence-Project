package com.shihab.practicesharedprefarence.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)


    fun saveUserName(name: String) {
        prefs.edit { putString("username", name) }
    }

    fun getUserName(): String {
        return prefs.getString("username", "") ?: ""

    }

    fun saveDarkMode(isDark: Boolean) {
        prefs.edit { putBoolean("dark_mode", isDark) }
    }

    fun getDarkMode(): Boolean {
        return prefs.getBoolean("dark_mode", false)
    }
}