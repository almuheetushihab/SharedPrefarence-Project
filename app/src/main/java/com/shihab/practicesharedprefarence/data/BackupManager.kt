package com.shihab.practicesharedprefarence.data

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.shihab.practicesharedprefarence.model.BackupData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class BackupManager(private val context: Context, private val dao: ExpenseDao) {
    private val gson = Gson()

    suspend fun exportData(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val expenses = dao.getAllExpensesList()
            val categories = dao.getAllCategoriesList()
            val backupData = BackupData(expenses, categories)
            val jsonString = gson.toJson(backupData)

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    writer.write(jsonString)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun exportToCSV(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val expenses = dao.getAllExpensesList()
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    // CSV Header
                    writer.write("ID,Type,Category,Amount,Note,Date\n")
                    // Data Rows
                    expenses.forEach { expense ->
                        val line = "${expense.id},${expense.type},${expense.category},${expense.amount},\"${expense.note}\",${expense.date}\n"
                        writer.write(line)
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun importData(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    val jsonString = reader.readText()
                    val backupData = gson.fromJson(jsonString, BackupData::class.java)

                    // Insert into database
                    dao.insertAllExpenses(backupData.expenses)
                    dao.insertAllCategories(backupData.categories)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
