package com.shihab.practicesharedprefarence.ui.screen.chartscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.shihab.practicesharedprefarence.ui.screen.expensescreen.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChartScreen(viewModel: ExpenseViewModel) {
    val dailyExpenses by viewModel.dailyExpenses.observeAsState(initial = emptyList())
    val dailyIncome by viewModel.dailyIncome.observeAsState(initial = emptyList())
    val categorySpending by viewModel.categorySpending.observeAsState(initial = emptyList())
    val totalExpense by viewModel.totalExpense.observeAsState(initial = 0L)
    val totalIncome by viewModel.totalIncome.observeAsState(initial = 0L)

    val modelProducer = remember { CartesianChartModelProducer() }

    // Combine income and expense by date for the chart
    LaunchedEffect(dailyExpenses, dailyIncome) {
        if (dailyExpenses.isNotEmpty() || dailyIncome.isNotEmpty()) {
            val allDates = (dailyExpenses.map { it.date } + dailyIncome.map { it.date }).distinct().sorted()
            val expenseValues = allDates.map { date -> dailyExpenses.find { it.date == date }?.totalAmount?.toFloat() ?: 0f }
            val incomeValues = allDates.map { date -> dailyIncome.find { it.date == date }?.totalAmount?.toFloat() ?: 0f }

            modelProducer.runTransaction {
                columnSeries {
                    series(incomeValues)
                    series(expenseValues)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Financial Analytics", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Summary Cards
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        SummaryMiniItem("Total Income", totalIncome ?: 0L, Color(0xFF4CAF50))
                        SummaryMiniItem("Total Expense", totalExpense ?: 0L, Color(0xFFF44336))
                    }
                }
            }

            item {
                Text(
                    text = "Income vs Expense Trend",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth().height(300.dp).padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    if (dailyExpenses.isEmpty() && dailyIncome.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No data available for charts", color = Color.Gray)
                        }
                    } else {
                        CartesianChartHost(
                            chart = rememberCartesianChart(
                                rememberColumnCartesianLayer()
                            ),
                            modelProducer = modelProducer,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Spending by Category",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp).fillMaxWidth()
                )
            }

            if (categorySpending.isEmpty()) {
                item {
                    Text("No expense categories found", color = Color.Gray, modifier = Modifier.padding(16.dp))
                }
            } else {
                items(categorySpending) { spending ->
                    val percentage = if ((totalExpense ?: 0L) > 0L) (spending.totalAmount.toFloat() / (totalExpense ?: 1L)) else 0f
                    CategoryProgressItem(spending.category, spending.totalAmount, percentage)
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun SummaryMiniItem(label: String, amount: Long, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text("৳$amount", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
    }
}

@Composable
fun CategoryProgressItem(category: String, amount: Long, percentage: Float) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(category, fontWeight = FontWeight.Bold)
                Text("৳$amount", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { percentage },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
            Text(
                text = "${(percentage * 100).toInt()}% of total expenses",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
            )
        }
    }
}
