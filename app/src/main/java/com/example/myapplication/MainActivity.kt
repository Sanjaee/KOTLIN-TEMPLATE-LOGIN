package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.navigation.NavGraph
import com.example.myapplication.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val repository by lazy { AuthRepository(this) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var startDestination by remember { mutableStateOf<String?>(null) }
                
                LaunchedEffect(Unit) {
                    val loggedIn = repository.isLoggedIn()
                    startDestination = if (loggedIn) Screen.Home.route else Screen.Login.route
                }
                
                // Show loading or nothing until we determine start destination
                startDestination?.let { destination ->
                    NavGraph(
                        startDestination = destination,
                        onLogout = {
                            lifecycleScope.launch {
                                repository.logout()
                            }
                        }
                    )
                } ?: run {
                    // Show loading indicator while checking login state
                    androidx.compose.material3.CircularProgressIndicator(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxSize()
                            .wrapContentSize(androidx.compose.ui.Alignment.Center)
                    )
                }
            }
        }
    }
}