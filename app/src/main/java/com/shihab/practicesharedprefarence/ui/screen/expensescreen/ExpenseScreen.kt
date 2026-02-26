package com.shihab.practicesharedprefarence.ui.screen.expensescreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var expanded by remember { mutableStateOf(false) }
    var showBudgetDialog by remember { mutableStateOf(false) }
    var newBudgetInput by remember { mutableStateOf("") }

    val categories = listOf("Food", "Transport", "Shopping", "Bills", "Health", "Other")

    val expenseList by viewModel.expenses.observeAsState(initial = emptyList())
    val totalAmountState by viewModel.totalAmount.observeAsState(initial = 0L)
    val totalAmount = totalAmountState ?: 0L
    val monthlyBudget by viewModel.monthlyBudget.observeAsState(initial = 0f)

    val progress = if (monthlyBudget > 0) totalAmount.toFloat() / monthlyBudget else 0f
    val progressColor by animateColorAsState(
        targetValue = when {
            progress >= 0.9f -> Color.Red
            progress >= 0.8f -> Color(0xFFFFA500) // Orange
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
            Text("Expense Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { 
                newBudgetInput = monthlyBudget.toString()
                showBudgetDialog = true 
            }) {
                Icon(Icons.Default.Edit, contentDescription = "Set Budget", tint = MaterialTheme.colorScheme.primary)
            }
        }

        // Budget Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total Spent", style = MaterialTheme.typography.labelLarge)
                        Text("৳ $totalAmount", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = if (progress >= 1f) Color.Red else Color.Unspecified)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Monthly Budget", style = MaterialTheme.typography.labelLarge)
                        Text("৳ ${monthlyBudget.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = progressColor,
                    trackColor = progressColor.copy(alpha = 0.2f)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}% used",
                        style = MaterialTheme.typography.bodySmall,
                        color = progressColor
                    )
                    if (monthlyBudget > 0) {
                        val remaining = (monthlyBudget - totalAmount).toInt()
                        Text(
                            text = if (remaining >= 0) "৳ $remaining left" else "Over budget by ৳ ${-remaining}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (remaining >= 0) Color.Gray else Color.Red
                        )
                    }
                }
            }
        }

        // Input Fields
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
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, "Dropdown", Modifier.clickable { expanded = true })
                    }
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                category = cat
                                expanded = false
                            }
                        )
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (amount.isNotEmpty()) {
                    viewModel.addExpense(amount, note.ifEmpty { "No note" }, category)
                    amount = ""; note = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text("Add Expense Record", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Recent Transactions", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(expenseList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                           Text(item.category.take(1), fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                        }
                        
                        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                            Text(item.note, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text("${item.category} • ${item.date}", fontSize = 12.sp, color = Color.Gray)
                        }
                        
                        Column(horizontalAlignment = Alignment.End) {
                            Text("৳${item.amount}", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.error)
                            IconButton(
                                onClick = { viewModel.deleteExpense(item) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
