package com.shihab.practicesharedprefarence.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shihab.practicesharedprefarence.model.Expense

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("expense_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUserName(name: String) = prefs.edit { putString("username", name) }
    fun getUserName(): String = prefs.getString("username", "") ?: ""

    fun saveExpenses(expenseList: List<Expense>) {
        val jsonString = gson.toJson(expenseList)
        prefs.edit { putString("expense_list", jsonString) }
    }

    fun getExpenses(): List<Expense> {
        val jsonString = prefs.getString("expense_list", null) ?: return emptyList()
        val type = object : TypeToken<List<Expense>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}