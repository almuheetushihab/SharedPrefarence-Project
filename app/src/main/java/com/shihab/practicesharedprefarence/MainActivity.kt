package com.shihab.practicesharedprefarence

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.shihab.practicesharedprefarence.ui.screen.chartscreen.ChartScreen
import com.shihab.practicesharedprefarence.ui.screen.expensescreen.ExpenseScreen
import com.shihab.practicesharedprefarence.ui.screen.expensescreen.ExpenseViewModel
import com.shihab.practicesharedprefarence.ui.theme.PracticeSharedPrefarenceTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeSharedPrefarenceTheme {
                val viewModel = ExpenseViewModel(application)
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
                        }
                    }
                ) { innerPadding ->
                    Modifier.padding(innerPadding).let {
                        if (currentScreen == "home") {
                            ExpenseScreen(viewModel = viewModel)
                        } else {
                            ChartScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
