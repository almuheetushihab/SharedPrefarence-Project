package com.shihab.practicesharedprefarence.ui.screen.expensescreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(viewModel: ExpenseViewModel) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    var transactionType by remember { mutableStateOf("Expense") } // "Income" or "Expense"
    var expanded by remember { mutableStateOf(false) }
    var showBudgetDialog by remember { mutableStateOf(false) }
    var newBudgetInput by remember { mutableStateOf("") }

    val categories = if (transactionType == "Expense") {
        listOf("Food", "Transport", "Shopping", "Bills", "Health", "Other")
    } else {
        listOf("Salary", "Freelance", "Gift", "Investment", "Other")
    }

    val expenseList by viewModel.expenses.observeAsState(initial = emptyList())
    val totalExpense by viewModel.totalExpense.observeAsState(initial = 0L)
    val totalIncome by viewModel.totalIncome.observeAsState(initial = 0L)
    val monthlyBudget by viewModel.monthlyBudget.observeAsState(initial = 0f)

    val currentBalance = (totalIncome ?: 0L) - (totalExpense ?: 0L)
    val progress = if (monthlyBudget > 0) (totalExpense ?: 0L).toFloat() / monthlyBudget else 0f
    val progressColor by animateColorAsState(
        targetValue = when {
            progress >= 0.9f -> Color.Red
            progress >= 0.8f -> Color(0xFFFFA500)
            else -> MaterialTheme.colorScheme.primary
        }, label = "progressColor"
    )

    if (showBudgetDialog) {
        AlertDialog(
            onDismissRequest = { showBudgetDialog = false },
            title = { Text("Set Monthly Budget") },
            text = {
                OutlinedTextField(
                    value = newBudgetInput,
                    onValueChange = { newBudgetInput = it },
                    label = { Text("Budget Amount (৳)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    val b = newBudgetInput.toFloatOrNull() ?: 0f
                    viewModel.updateBudget(b)
                    showBudgetDialog = false
                    newBudgetInput = ""
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBudgetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Finance Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { 
                newBudgetInput = monthlyBudget.toString()
                showBudgetDialog = true 
            }) {
                Icon(Icons.Default.Edit, contentDescription = "Set Budget", tint = MaterialTheme.colorScheme.primary)
            }
        }

        // Main Balance Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Total Balance", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelLarge)
                Text("৳ $currentBalance", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TransactionSummaryItem(label = "Income", amount = totalIncome ?: 0L, icon = Icons.Default.TrendingUp, color = Color(0xFF4CAF50))
                    TransactionSummaryItem(label = "Expense", amount = totalExpense ?: 0L, icon = Icons.Default.TrendingDown, color = Color(0xFFFFCDD2))
                }
            }
        }

        // Budget Progress
        if (monthlyBudget > 0) {
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Monthly Budget Limit", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text("৳ ${monthlyBudget.toInt()}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                    color = progressColor,
                    trackColor = progressColor.copy(alpha = 0.2f)
                )
            }
        }

        // Input Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Type Switcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    TypeToggleButton(text = "Expense", isSelected = transactionType == "Expense", onClick = { transactionType = "Expense"; category = "Food" }, color = Color.Red, modifier = Modifier.weight(1f))
                    TypeToggleButton(text = "Income", isSelected = transactionType == "Income", onClick = { transactionType = "Income"; category = "Salary" }, color = Color(0xFF4CAF50), modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Dropdown", Modifier.clickable { expanded = true }) }
                        )
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            categories.forEach { cat ->
                                DropdownMenuItem(text = { Text(cat) }, onClick = { category = cat; expanded = false })
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (amount.isNotEmpty()) {
                            viewModel.addTransaction(amount, note.ifEmpty { "No note" }, category, transactionType)
                            amount = ""; note = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add $transactionType")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Recent Transactions", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(expenseList) { item ->
                TransactionItem(item, onDelete = { viewModel.deleteExpense(item) })
            }
        }
    }
}

@Composable
fun TransactionSummaryItem(label: String, amount: Long, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(32.dp).background(Color.White.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(label, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
            Text("৳ $amount", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun TypeToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit, color: Color, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) color.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (isSelected) color else Color.Gray, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun TransactionItem(item: com.shihab.practicesharedprefarence.model.Expense, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            val isIncome = item.type == "Income"
            Box(
                modifier = Modifier.size(40.dp).background(if (isIncome) Color(0xFFE8F5E9) else Color(0xFFFFEBEE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (isIncome) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336),
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(item.note, fontWeight = FontWeight.Bold)
                Text("${item.category} • ${item.date}", fontSize = 11.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${if (isIncome) "+" else "-"} ৳${item.amount}",
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(20.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
