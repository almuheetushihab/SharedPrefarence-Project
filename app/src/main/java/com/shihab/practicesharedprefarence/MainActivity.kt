package com.shihab.practicesharedprefarence

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.shihab.practicesharedprefarence.data.PreferenceManager
import com.shihab.practicesharedprefarence.ui.screen.chartscreen.ChartScreen
import com.shihab.practicesharedprefarence.ui.screen.expensescreen.ExpenseScreen
import com.shihab.practicesharedprefarence.ui.screen.expensescreen.ExpenseViewModel
import com.shihab.practicesharedprefarence.ui.screen.settingscreen.SettingsScreen
import com.shihab.practicesharedprefarence.ui.screen.settingscreen.SettingsViewModel
import com.shihab.practicesharedprefarence.ui.theme.PracticeSharedPrefarenceTheme

class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val preferenceManager = PreferenceManager(applicationContext)
        
        setContent {
            val settingsViewModel = SettingsViewModel(application)
            val isDarkMode by settingsViewModel.isDarkMode
            var isUnlocked by remember { mutableStateOf(!preferenceManager.isBiometricEnabled()) }

            PracticeSharedPrefarenceTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isUnlocked) {
                        MainContent(settingsViewModel)
                    } else {
                        LockedScreen(onUnlockRequest = {
                            showBiometricPrompt { success ->
                                isUnlocked = success
                            }
                        })
                        
                        LaunchedEffect(Unit) {
                            showBiometricPrompt { success ->
                                isUnlocked = success
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun LockedScreen(onUnlockRequest: () -> Unit) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "App Locked",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Please unlock to continue",
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onUnlockRequest) {
                Text("Unlock Now")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun MainContent(settingsViewModel: SettingsViewModel) {
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

    private fun showBiometricPrompt(onResult: (Boolean) -> Unit) {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val executor = ContextCompat.getMainExecutor(this)
                val biometricPrompt = BiometricPrompt(this, executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            if (errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                                Toast.makeText(applicationContext, "Error: $errString", Toast.LENGTH_SHORT).show()
                            }
                            onResult(false)
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            onResult(true)
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            onResult(false)
                        }
                    })

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("App Lock")
                    .setSubtitle("Authenticate to use the app")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    .build()

                biometricPrompt.authenticate(promptInfo)
            }
            else -> {
                // If biometric is not available, just unlock
                onResult(true)
            }
        }
    }
}
