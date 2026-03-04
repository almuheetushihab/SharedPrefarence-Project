package com.shihab.practicesharedprefarence.ui.screen.settingscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val name by viewModel.userName
    val darkMode by viewModel.isDarkMode
    val selectedCurrency by viewModel.currency
    val backupStatus by viewModel.backupStatus

    var textFieldValue by remember(name) { mutableStateOf(name) }
    val currencies = listOf("৳", "$", "€", "£")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("User Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("User Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.saveUserName(textFieldValue) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Name")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode")
            Switch(
                checked = darkMode,
                onCheckedChange = { viewModel.toggleDarkMode(it) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Currency Symbol", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        currencies.forEach { currency ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.saveCurrency(currency) }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedCurrency == currency,
                    onClick = { viewModel.saveCurrency(currency) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = currency)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Backup & Restore", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { viewModel.exportData() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Export Data")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { viewModel.importData() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Import Data")
            }
        }

        if (backupStatus.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = backupStatus,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}
