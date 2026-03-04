package com.shihab.practicesharedprefarence.model

import com.shihab.practicesharedprefarence.data.Category

data class BackupData(
    val expenses: List<Expense>,
    val categories: List<Category>
)
