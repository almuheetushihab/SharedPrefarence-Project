package com.shihab.practicesharedprefarence.data

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("expense_prefs", Context.MODE_PRIVATE)

    fun saveUserName(name: String) {
        prefs.edit().putString("username", name).apply()
    }
    
    fun getUserName(): String = prefs.getString("username", "") ?: ""

    fun saveBudget(amount: Float) {
        prefs.edit().putFloat("monthly_budget", amount).apply()
    }

    fun getBudget(): Float = prefs.getFloat("monthly_budget", 0f)

    fun setDarkMode(isDark: Boolean) {
        prefs.edit().putBoolean("is_dark_mode", isDark).apply()
    }

    fun isDarkMode(): Boolean = prefs.getBoolean("is_dark_mode", false)

    fun saveCurrency(currency: String) {
        prefs.edit().putString("currency_symbol", currency).apply()
    }

    fun getCurrency(): String = prefs.getString("currency_symbol", "৳") ?: "৳"

    fun setBiometricEnabled(isEnabled: Boolean) {
        prefs.edit().putBoolean("biometric_enabled", isEnabled).apply()
    }

    fun isBiometricEnabled(): Boolean = prefs.getBoolean("biometric_enabled", false)
}
