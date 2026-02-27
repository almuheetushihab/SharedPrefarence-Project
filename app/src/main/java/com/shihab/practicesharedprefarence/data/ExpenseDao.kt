package com.shihab.practicesharedprefarence.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shihab.practicesharedprefarence.model.Category
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

    @Query("SELECT date, SUM(CAST(amount AS BIGINT)) as totalAmount FROM expense_table WHERE type = 'Income' GROUP BY date ORDER BY date ASC")
    fun getDailyIncome(): Flow<List<DailyExpense>>

    @Query("SELECT category, SUM(CAST(amount AS BIGINT)) as totalAmount FROM expense_table WHERE type = 'Expense' GROUP BY category")
    fun getCategoryWiseSpending(): Flow<List<CategorySpending>>

    // Category Methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM category_table WHERE type = :type ORDER BY name ASC")
    fun getCategoriesByType(type: String): Flow<List<Category>>
}

data class DailyExpense(
    val date: String,
    val totalAmount: Long
)

data class CategorySpending(
    val category: String,
    val totalAmount: Long
)
