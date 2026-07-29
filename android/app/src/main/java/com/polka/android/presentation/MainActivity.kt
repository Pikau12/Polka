package com.polka.android.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.polka.android.presentation.navigation.PolkaNavHost
import com.polka.android.presentation.theme.PolkaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PolkaTheme {
                PolkaApp()
            }
        }
    }
}

@Composable
fun PolkaApp() {
    val navController = rememberNavController()
    PolkaNavHost(navController = navController)
}