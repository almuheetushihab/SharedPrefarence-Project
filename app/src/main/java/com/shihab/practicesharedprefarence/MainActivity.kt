package com.shihab.practicesharedprefarence

import SettingsViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.shihab.practicesharedprefarence.ui.screen.expensescreen.ExpenseScreen
import com.shihab.practicesharedprefarence.ui.screen.expensescreen.ExpenseViewModel
import com.shihab.practicesharedprefarence.ui.screen.settingscreen.SettingsScreen
import com.shihab.practicesharedprefarence.ui.theme.PracticeSharedPrefarenceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeSharedPrefarenceTheme {

                ExpenseScreen(viewModel = ExpenseViewModel(application))

//                SettingsScreen(
//                    viewModel = SettingsViewModel(application)
//                )
            }
        }
    }
}
