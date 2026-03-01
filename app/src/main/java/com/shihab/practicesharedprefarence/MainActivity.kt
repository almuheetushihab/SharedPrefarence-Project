package com.shihab.practicesharedprefarence

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.shihab.practicesharedprefarence.ui.screen.chartscreen.ChartScreen
import com.shihab.practicesharedprefarence.ui.screen.expensescreen.ExpenseScreen
import com.shihab.practicesharedprefarence.ui.screen.expensescreen.ExpenseViewModel
import com.shihab.practicesharedprefarence.ui.screen.settingscreen.SettingsScreen
import com.shihab.practicesharedprefarence.ui.screen.settingscreen.SettingsViewModel
import com.shihab.practicesharedprefarence.ui.theme.PracticeSharedPrefarenceTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel = SettingsViewModel(application)
            val isDarkMode by settingsViewModel.isDarkMode

            PracticeSharedPrefarenceTheme(darkTheme = isDarkMode) {
                val expenseViewModel = ExpenseViewModel(application)
                var currentScreen by remember { mutableStateOf("home") }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentScreen == "home",
                                onClick = { currentScreen = "home" },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home") }
                            )
                            NavigationBarItem(
                                selected = currentScreen == "chart",
                                onClick = { currentScreen = "chart" },
                                icon = { Icon(Icons.Default.Info, contentDescription = "Chart") },
                                label = { Text("Analytics") }
                            )
                            NavigationBarItem(
                                selected = currentScreen == "settings",
                                onClick = { currentScreen = "settings" },
                                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                label = { Text("Settings") }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            "home" -> ExpenseScreen(viewModel = expenseViewModel)
                            "chart" -> ChartScreen(viewModel = expenseViewModel)
                            "settings" -> SettingsScreen(viewModel = settingsViewModel)
                        }
                    }
                }
            }
        }
    }
}
