package com.shihab.practicesharedprefarence.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shihab.practicesharedprefarence.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expense_table ORDER BY id DESC")
    fun getAllExpenses(): kotlinx.coroutines.flow.Flow<List<Expense>>

    @Query("SELECT SUM(CAST(amount AS INTEGER)) FROM expense_table")
    fun getTotalAmount(): kotlinx.coroutines.flow.Flow<Int?>
}