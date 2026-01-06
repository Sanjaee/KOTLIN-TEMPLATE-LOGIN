package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false,
    val requiresVerification: Boolean = false,
    val email: String = ""
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            // Trim whitespace and newline characters
            val trimmedEmail = email.trim()
            val trimmedPassword = password.trim()
            
            val request = LoginRequest(email = trimmedEmail, password = trimmedPassword)
            repository.login(request).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoginSuccess = true,
                        errorMessage = null
                    )
                },
                onFailure = { error ->
                    val errorMsg = error.message ?: "Login failed"
                    val requiresVerification = errorMsg.contains("not verified", ignoreCase = true)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = errorMsg,
                        requiresVerification = requiresVerification,
                        email = if (requiresVerification) trimmedEmail else ""
                    )
                }
            )
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun resetSuccessState() {
        _uiState.value = _uiState.value.copy(isLoginSuccess = false)
    }
}

