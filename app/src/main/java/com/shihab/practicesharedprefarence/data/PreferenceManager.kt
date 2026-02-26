package com.shihab.practicesharedprefarence.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("expense_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUserName(name: String) = prefs.edit { putString("username", name) }
    fun getUserName(): String = prefs.getString("username", "") ?: ""

    fun saveBudget(amount: Float) = prefs.edit { putFloat("monthly_budget", amount) }
    fun getBudget(): Float = prefs.getFloat("monthly_budget", 0f)
}
