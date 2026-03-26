package com.shihab.practicesharedprefarence.ui.screen.settingscreen

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.shihab.practicesharedprefarence.data.ExpenseDatabase
import com.shihab.practicesharedprefarence.data.PreferenceManager
import com.shihab.practicesharedprefarence.model.BackupData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val pref = PreferenceManager(application)
    private val database = ExpenseDatabase.getDatabase(application)
    private val dao = database.expenseDao()

    private val _userName = mutableStateOf("")
    val userName: State<String> = _userName

    private val _isDarkMode = mutableStateOf(false)
    val isDarkMode: State<Boolean> = _isDarkMode

    private val _currency = mutableStateOf("৳")
    val currency: State<String> = _currency

    private val _isBiometricEnabled = mutableStateOf(false)
    val isBiometricEnabled: State<Boolean> = _isBiometricEnabled

    private val _backupStatus = mutableStateOf("")
    val backupStatus: State<String> = _backupStatus

    init {
        loadData()
    }

    private fun loadData() {
        _userName.value = pref.getUserName()
        _isDarkMode.value = pref.isDarkMode()
        _currency.value = pref.getCurrency()
        _isBiometricEnabled.value = pref.isBiometricEnabled()
    }

    fun saveUserName(name: String) {
        _userName.value = name
        pref.saveUserName(name)
    }

    fun toggleDarkMode(isDark: Boolean) {
        _isDarkMode.value = isDark
        pref.setDarkMode(isDark)
    }

    fun saveCurrency(symbol: String) {
        _currency.value = symbol
        pref.saveCurrency(symbol)
    }

    fun toggleBiometric(isEnabled: Boolean) {
        _isBiometricEnabled.value = isEnabled
        pref.setBiometricEnabled(isEnabled)
    }

    fun exportData() {
        viewModelScope.launch {
            try {
                val expenses = dao.getAllExpensesList()
                val categories = dao.getAllCategoriesList()
                val backupData = BackupData(expenses, categories)
                val json = Gson().toJson(backupData)

                withContext(Dispatchers.IO) {
                    val file = File(getApplication<Application>().getExternalFilesDir(null), "expense_backup.json")
                    file.writeText(json)
                }
                _backupStatus.value = "Data exported successfully to downloads folder"
            } catch (e: Exception) {
                _backupStatus.value = "Export failed: ${e.message}"
            }
        }
    }

    fun importData() {
        viewModelScope.launch {
            try {
                val json = withContext(Dispatchers.IO) {
                    val file = File(getApplication<Application>().getExternalFilesDir(null), "expense_backup.json")
                    if (file.exists()) file.readText() else null
                }

                if (json != null) {
                    val backupData = Gson().fromJson(json, BackupData::class.java)
                    dao.insertAllExpenses(backupData.expenses)
                    dao.insertAllCategories(backupData.categories)
                    _backupStatus.value = "Data imported successfully"
                } else {
                    _backupStatus.value = "Backup file not found"
                }
            } catch (e: Exception) {
                _backupStatus.value = "Import failed: ${e.message}"
            }
        }
    }
}
