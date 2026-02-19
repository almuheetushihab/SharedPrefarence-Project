package com.shihab.practicesharedprefarence.ui.screen.settingscreen

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.shihab.practicesharedprefarence.data.PreferenceManager

class SettingsViewModel( application: Application) : AndroidViewModel(application) {
    private val preferenceManager = PreferenceManager(application)

    private val pref = PreferenceManager(application)

    var userName = mutableSetOf("")
        private set

    var isDarkMode = mutableStateOf(false)
        private set

    init {
        loadData()
    }

    private fun loadData() {
        userName.value = pref.getUserName()
        isDarkMode.value = pref.getDarkMode()
    }

    fun saveUserName(name: String) {
        userName.value = name
        pref.saveUserName(name)
    }

    fun toggleDarkMode(isDark: Boolean) {
        isDarkMode.value = isDark
        pref.saveDarkMode(isDark)
    }
}