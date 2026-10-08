package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.navigation.AppNavigation
import com.example.myapplication.ui.theme.HealthNexaTheme
import com.example.myapplication.ui.viewmodel.HealthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthNexaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val healthViewModel: HealthViewModel = viewModel()
                    AppNavigation(viewModel = healthViewModel)
                }
            }
        }
    }
}
