package com.shihab.practicesharedprefarence.ui.screen.expensescreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.shihab.practicesharedprefarence.data.ExpenseDatabase
import com.shihab.practicesharedprefarence.data.PreferenceManager
import com.shihab.practicesharedprefarence.model.Expense
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ExpenseDatabase.getDatabase(application)
    private val dao = database.expenseDao()
    private val preferenceManager = PreferenceManager(application)

    val expenses = dao.getAllExpenses().asLiveData()
    val totalAmount = dao.getTotalAmount().asLiveData()
    val dailyExpenses = dao.getDailyExpenses().asLiveData()

    private val _monthlyBudget = MutableLiveData<Float>(preferenceManager.getBudget())
    val monthlyBudget: LiveData<Float> = _monthlyBudget

    fun updateBudget(amount: Float) {
        preferenceManager.saveBudget(amount)
        _monthlyBudget.value = amount
    }

    fun addExpense(amountStr: String, note: String, category: String) {
        val sdf = SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.getDefault())
        val currentDate = sdf.format(Date())

        val amountLong = amountStr.toLongOrNull() ?: 0L

        val newExpense = Expense(
            amount = amountLong,
            note = note,
            category = category,
            date = currentDate
        )

        viewModelScope.launch {
            dao.insertExpense(newExpense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            dao.deleteExpense(expense)
        }
    }
}
