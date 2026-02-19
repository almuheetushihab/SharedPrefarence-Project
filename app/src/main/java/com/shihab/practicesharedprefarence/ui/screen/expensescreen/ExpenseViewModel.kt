package com.shihab.practicesharedprefarence.ui.screen.expensescreen

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.shihab.practicesharedprefarence.data.PreferenceManager
import com.shihab.practicesharedprefarence.model.Expense
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    private val pref = PreferenceManager(application)

    private val _expenses = mutableStateOf(pref.getExpenses())
    val expenses: State<List<Expense>> = _expenses

    fun addExpense(amount: String, note: String) {
        val sdf = SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.getDefault())
        val currentDate = sdf.format(Date())

        val newExpense = Expense(
            id = System.currentTimeMillis(),
            amount = amount,
            note = note,
            date = currentDate
        )

        val updatedList = _expenses.value.toMutableList().apply { add(0, newExpense) }
        _expenses.value = updatedList
        pref.saveExpenses(updatedList)
    }

    fun deleteExpense(expense: Expense) {
        val updatedList = _expenses.value.toMutableList().apply { remove(expense) }
        _expenses.value = updatedList
        pref.saveExpenses(updatedList)
    }

    fun getTotalAmount(): Int {
        return _expenses.value.sumOf { it.amount.toIntOrNull() ?: 0 }
    }
}