package com.shihab.practicesharedprefarence.ui.screen.expensescreen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(viewModel: ExpenseViewModel) {
    val context = LocalContext.current
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var transactionType by remember { mutableStateOf("Expense") }
    var expanded by remember { mutableStateOf(false) }
    var showBudgetDialog by remember { mutableStateOf(false) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var showBackupMenu by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var newBudgetInput by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val expenseList by viewModel.expenses.observeAsState(initial = emptyList())
    val totalExpense by viewModel.totalExpense.observeAsState(initial = 0L)
    val totalIncome by viewModel.totalIncome.observeAsState(initial = 0L)
    val monthlyBudget by viewModel.monthlyBudget.observeAsState(initial = 0f)
    val currencySymbol by viewModel.currencySymbol.observeAsState(initial = "৳")
    
    val categoryList by viewModel.getCategories(transactionType).observeAsState(initial = emptyList())

    // CSV Export Launcher
    val exportCSVLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            viewModel.exportToCSV(it) { success ->
                Toast.makeText(context, if (success) "CSV exported successfully!" else "Failed to export CSV", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // JSON Backup Launcher
    val createBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            viewModel.exportData(it) { success ->
                Toast.makeText(context, if (success) "Backup saved successfully!" else "Failed to save backup", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Restore Backup Launcher
    val restoreBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            viewModel.importData(it) { success ->
                Toast.makeText(context, if (success) "Data restored successfully!" else "Failed to restore data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(categoryList, transactionType) {
        if (category.isEmpty() || !categoryList.any { it.name == category }) {
            category = categoryList.firstOrNull()?.name ?: ""
        }
    }

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
                    label = { Text("Budget Amount ($currencySymbol)") },
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
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showBudgetDialog = false }) { Text("Cancel") } }
        )
    }

    if (showAddCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showAddCategoryDialog = false },
            title = { Text("Add New $transactionType Category") },
            text = {
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (newCategoryName.isNotEmpty()) {
                        viewModel.addCategory(newCategoryName, transactionType)
                        showAddCategoryDialog = false
                        newCategoryName = ""
                    }
                }) { Text("Add") }
            },
            dismissButton = { TextButton(onClick = { showAddCategoryDialog = false }) { Text("Cancel") } }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Finance Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            
            Box {
                IconButton(onClick = { showBackupMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = MaterialTheme.colorScheme.primary)
                }
                DropdownMenu(
                    expanded = showBackupMenu,
                    onDismissRequest = { showBackupMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Set Budget") },
                        onClick = { 
                            showBackupMenu = false
                            newBudgetInput = monthlyBudget.toString()
                            showBudgetDialog = true 
                        },
                        leadingIcon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Export to CSV (Excel)") },
                        onClick = { 
                            showBackupMenu = false
                            val fileName = "expenses_${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())}.csv"
                            exportCSVLauncher.launch(fileName)
                        },
                        leadingIcon = { Icon(Icons.Default.FileDownload, contentDescription = null) }
                    )
                    DropdownMenuItem(
                        text = { Text("Full Backup (JSON)") },
                        onClick = { 
                            showBackupMenu = false
                            val fileName = "finance_backup_${SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())}.json"
                            createBackupLauncher.launch(fileName)
                        },
                        leadingIcon = { Icon(Icons.Default.Backup, contentDescription = null) }
                    )
                    DropdownMenuItem(
                        text = { Text("Restore Data") },
                        onClick = { 
                            showBackupMenu = false
                            restoreBackupLauncher.launch(arrayOf("application/json"))
                        },
                        leadingIcon = { Icon(Icons.Default.Restore, contentDescription = null) }
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Total Balance", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelLarge)
                Text("$currencySymbol $currentBalance", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TransactionSummaryItem(label = "Income", amount = totalIncome ?: 0L, icon = Icons.Default.TrendingUp, color = Color(0xFF4CAF50), currency = currencySymbol)
                    TransactionSummaryItem(label = "Expense", amount = totalExpense ?: 0L, icon = Icons.Default.TrendingDown, color = Color(0xFFFFCDD2), currency = currencySymbol)
                }
            }
        }

        if (monthlyBudget > 0) {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                    color = progressColor,
                    trackColor = progressColor.copy(alpha = 0.2f)
                )
                Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Budget: $currencySymbol$monthlyBudget", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text("${(progress * 100).toInt()}% used", style = MaterialTheme.typography.bodySmall, color = progressColor, fontWeight = FontWeight.Bold)
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).padding(4.dp)) {
                    TypeToggleButton(text = "Expense", isSelected = transactionType == "Expense", onClick = { transactionType = "Expense" }, color = Color(0xFFF44336), modifier = Modifier.weight(1f))
                    TypeToggleButton(text = "Income", isSelected = transactionType == "Income", onClick = { transactionType = "Income" }, color = Color(0xFF4CAF50), modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Amount") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1.2f)) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            trailingIcon = { 
                                IconButton(onClick = { expanded = true }) {
                                    Icon(Icons.Default.ArrowDropDown, "Select")
                                }
                            },
                            leadingIcon = { 
                                Icon(
                                    Icons.Default.AddCircleOutline, 
                                    "Add Category", 
                                    Modifier.size(20.dp).clickable { showAddCategoryDialog = true },
                                    tint = MaterialTheme.colorScheme.primary
                                ) 
                            }
                        )
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            categoryList.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.name) },
                                    onClick = { category = cat.name; expanded = false },
                                    trailingIcon = {
                                        IconButton(onClick = { viewModel.deleteCategory(cat) }, modifier = Modifier.size(24.dp)) {
                                            Icon(Icons.Default.Close, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                                        }
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
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (amount.isNotEmpty() && category.isNotEmpty()) {
                            viewModel.addTransaction(amount, note.ifEmpty { "Transaction" }, category, transactionType)
                            amount = ""; note = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = amount.isNotEmpty() && category.isNotEmpty()
                ) {
                    Text("Add $transactionType", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                viewModel.updateSearchQuery(it)
            },
            placeholder = { Text("Search by note or category...") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text("Transaction History", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(expenseList) { item ->
                TransactionItem(item, currencySymbol, onDelete = { viewModel.deleteExpense(item) })
            }
            if (expenseList.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No transactions found", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionSummaryItem(label: String, amount: Long, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, currency: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(36.dp).background(Color.White.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(label, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
            Text("$currency$amount", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Composable
fun TypeToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit, color: Color, modifier: Modifier) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) color.copy(alpha = 0.15f) else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (isSelected) color else Color.Gray, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun TransactionItem(item: com.shihab.practicesharedprefarence.model.Expense, currency: String, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            val isIncome = item.type == "Income"
            Box(
                modifier = Modifier.size(44.dp).background(if (isIncome) Color(0xFFE8F5E9) else Color(0xFFFFEBEE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (isIncome) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(item.note, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${item.category} • ${item.date}", fontSize = 12.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${if (isIncome) "+" else "-"} $currency${item.amount}",
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336),
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.LightGray.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
