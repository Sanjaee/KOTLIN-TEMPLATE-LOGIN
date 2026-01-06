package com.example.myapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.components.OtpInputField
import com.example.myapplication.ui.viewmodel.VerifyOTPViewModel
import com.example.myapplication.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyOTPScreen(
    email: String,
    onVerifySuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: VerifyOTPViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    var otpCode by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.isVerifySuccess) {
        if (uiState.isVerifySuccess) {
            onVerifySuccess()
            viewModel.resetSuccessState()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Verify OTP") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Enter the OTP sent to:",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OtpInputField(
                otpText = otpCode,
                onOtpTextChange = { 
                    if (it.length <= 6) {
                        otpCode = it
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp),
                enabled = !uiState.isLoading
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { viewModel.verifyOTP(email, otpCode) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && otpCode.length == 6
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Verify OTP")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(onClick = { viewModel.resendOTP(email) }) {
                Text("Resend OTP")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(onClick = onNavigateToLogin) {
                Text("Back to Login")
            }
            
            if (uiState.isResendSuccess) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "OTP sent successfully!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            uiState.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

