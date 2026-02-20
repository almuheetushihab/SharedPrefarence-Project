package com.shihab.practicesharedprefarence.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Long,
    val note: String,
    val category: String = "Other", // নতুন ফিল্ড যোগ করা হলো
    val date: String
)
