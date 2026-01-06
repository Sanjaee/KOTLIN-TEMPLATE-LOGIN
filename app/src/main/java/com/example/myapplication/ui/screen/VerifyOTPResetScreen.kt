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
import com.example.myapplication.ui.viewmodel.ForgotPasswordViewModel
import com.example.myapplication.ui.viewmodel.ViewModelFactory

/**
 * A Composable screen responsible for verifying the One-Time Password (OTP) during the password reset process.
 *
 * This screen displays an input field for the user to enter the 6-digit OTP sent to their email.
 * It handles the interaction with the [ForgotPasswordViewModel] to verify the OTP or resend it.
 * Upon successful verification, it triggers the [onOtpVerified] callback to proceed to the next step (resetting the password).
 *
 * @param email The email address to which the OTP was sent. Displayed to the user for confirmation.
 * @param onOtpVerified A callback invoked when the OTP is successfully verified. It passes the [email] and the verified `otpCode` as arguments.
 * @param onNavigateToLogin A callback invoked when the user chooses to navigate back to the login screen.
 * @param viewModel The [ForgotPasswordViewModel] managing the state and business logic for OTP verification. Defaults to a standard instance via [ViewModelFactory].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyOTPResetScreen(
    email: String,
    onOtpVerified: (String, String) -> Unit, // email, otpCode
    onNavigateToLogin: () -> Unit,
    viewModel: ForgotPasswordViewModel = viewModel(
        factory = ViewModelFactory(LocalContext.current.applicationContext as android.app.Application)
    )
) {
    var otpCode by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.isOtpVerified) {
        if (uiState.isOtpVerified) {
            onOtpVerified(email, uiState.verifiedOtpCode)
            viewModel.resetOtpVerifiedState()
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Enter OTP Code",
                style = MaterialTheme.typography.headlineSmall
            )
            
            Text(
                text = "Please enter the 6-digit OTP code sent to your email: $email",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
                onClick = {
                    if (otpCode.length == 6) {
                        viewModel.verifyResetOTP(email, otpCode)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && otpCode.length == 6
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Verify OTP")
                }
            }
            
            TextButton(
                onClick = {
                    viewModel.sendResetOTP(email)
                },
                enabled = !uiState.isLoading
            ) {
                Text("Resend OTP")
            }
            
            TextButton(onClick = onNavigateToLogin) {
                Text("Back to Login")
            }
            
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
