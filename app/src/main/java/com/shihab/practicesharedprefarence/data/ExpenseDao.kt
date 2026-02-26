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
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT SUM(CAST(amount AS BIGINT)) FROM expense_table WHERE type = 'Expense'")
    fun getTotalExpense(): Flow<Long?>

    @Query("SELECT SUM(CAST(amount AS BIGINT)) FROM expense_table WHERE type = 'Income'")
    fun getTotalIncome(): Flow<Long?>

    @Query("SELECT date, SUM(CAST(amount AS BIGINT)) as totalAmount FROM expense_table WHERE type = 'Expense' GROUP BY date ORDER BY date ASC")
    fun getDailyExpenses(): Flow<List<DailyExpense>>
}

data class DailyExpense(
    val date: String,
    val totalAmount: Long
)
