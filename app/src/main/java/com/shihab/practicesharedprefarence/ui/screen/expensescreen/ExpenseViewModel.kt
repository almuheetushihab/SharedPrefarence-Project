package com.shihab.practicesharedprefarence.ui.screen.expensescreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.shihab.practicesharedprefarence.data.ExpenseDatabase
import com.shihab.practicesharedprefarence.model.Expense
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ExpenseDatabase.getDatabase(application)
    private val dao = database.expenseDao()

    val expenses = dao.getAllExpenses().asLiveData()
    val totalAmount = dao.getTotalAmount().asLiveData()

    fun addExpense(amountStr: String, note: String) {
        val sdf = SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.getDefault())
        val currentDate = sdf.format(Date())

        val amountLong = amountStr.toLongOrNull() ?: 0L

        val newExpense = Expense(
            amount = amountLong,
            note = note,
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