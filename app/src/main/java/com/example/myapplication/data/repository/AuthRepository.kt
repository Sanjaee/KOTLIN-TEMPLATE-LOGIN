package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.api.ApiClient
import com.example.myapplication.data.model.*
import com.example.myapplication.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import retrofit2.Response

class AuthRepository(private val context: Context) {
    private val apiService = ApiClient.authApiService
    private val preferencesManager = PreferencesManager(context)
    
    suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = apiService.register(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val isSuccess = responseBody?.success != false && responseBody?.data != null
                if (isSuccess) {
                    responseBody?.data?.let {
                        Result.success(it)
                    } ?: Result.failure(Exception("Response data is null"))
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = apiService.login(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                // Check if success field is true, or if it's null/true and we have data (backward compatibility)
                val isSuccess = responseBody?.success != false && responseBody?.data != null
                
                if (isSuccess) {
                    responseBody?.data?.let { authResponse ->
                        // Save tokens
                        preferencesManager.saveTokens(
                            authResponse.accessToken,
                            authResponse.refreshToken,
                            authResponse.user.email
                        )
                        Result.success(authResponse)
                    } ?: Result.failure(Exception("Response data is null"))
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } else {
                // Check if it's unverified email error
                if (response.code() == 401) {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string()) ?: "Email not verified"
                    Result.failure(Exception(errorMessage))
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun verifyOTP(request: VerifyOTPRequest): Result<AuthResponse> {
        return try {
            val response = apiService.verifyOTP(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val isSuccess = responseBody?.success != false && responseBody?.data != null
                if (isSuccess) {
                    responseBody?.data?.let { authResponse ->
                        // Save tokens
                        preferencesManager.saveTokens(
                            authResponse.accessToken,
                            authResponse.refreshToken,
                            authResponse.user.email
                        )
                        Result.success(authResponse)
                    } ?: Result.failure(Exception("Response data is null"))
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun parseErrorMessage(errorBody: String?): String {
        if (errorBody.isNullOrBlank()) {
            return "An error occurred"
        }
        
        return try {
            // Try to parse JSON error response
            val jsonObject = org.json.JSONObject(errorBody)
            
            // Try different error response formats
            when {
                jsonObject.has("error") -> {
                    val errorObj = jsonObject.getJSONObject("error")
                    errorObj.optString("message", "An error occurred")
                }
                jsonObject.has("message") -> {
                    jsonObject.optString("message", "An error occurred")
                }
                jsonObject.has("error") && jsonObject.get("error") is String -> {
                    jsonObject.getString("error")
                }
                else -> errorBody
            }
        } catch (e: Exception) {
            // If JSON parsing fails, return the raw error body or a default message
            errorBody.takeIf { it.length < 200 } ?: "An error occurred"
        }
    }
    
    suspend fun resendOTP(request: ResendOTPRequest): Result<Unit> {
        return try {
            val response = apiService.resendOTP(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val isSuccess = responseBody?.success != false
                if (isSuccess) {
                    Result.success(Unit)
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun forgotPassword(request: ForgotPasswordRequest): Result<Unit> {
        return try {
            val response = apiService.forgotPassword(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val isSuccess = responseBody?.success != false
                if (isSuccess) {
                    Result.success(Unit)
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun verifyResetPassword(request: VerifyResetPasswordRequest): Result<Unit> {
        return try {
            val response = apiService.verifyResetPassword(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val isSuccess = responseBody?.success != false
                if (isSuccess) {
                    Result.success(Unit)
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun verifyEmail(request: VerifyEmailRequest): Result<AuthResponse> {
        return try {
            val response = apiService.verifyEmail(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val isSuccess = responseBody?.success != false && responseBody?.data != null
                if (isSuccess) {
                    responseBody?.data?.let { authResponse ->
                        // Save tokens
                        preferencesManager.saveTokens(
                            authResponse.accessToken,
                            authResponse.refreshToken,
                            authResponse.user.email
                        )
                        Result.success(authResponse)
                    } ?: Result.failure(Exception("Response data is null"))
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun isLoggedIn(): Boolean {
        return preferencesManager.accessToken.first() != null
    }
    
    suspend fun logout() {
        preferencesManager.clearTokens()
    }
    
    suspend fun getAccessToken(): String? {
        return preferencesManager.accessToken.first()
    }
    
    suspend fun getCurrentUser(): Result<User> {
        return try {
            val token = getAccessToken() ?: return Result.failure(Exception("Not authenticated"))
            val response = apiService.getMe("Bearer $token")
            if (response.isSuccessful) {
                val responseBody = response.body()
                val isSuccess = responseBody?.success != false && responseBody?.data != null
                if (isSuccess) {
                    responseBody?.data?.get("user")?.let { user ->
                        Result.success(user as User)
                    } ?: Result.failure(Exception("User data not found"))
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun googleOAuth(request: GoogleOAuthRequest): Result<AuthResponse> {
        return try {
            val response = apiService.googleOAuth(request)
            if (response.isSuccessful) {
                val responseBody = response.body()
                val isSuccess = responseBody?.success != false && responseBody?.data != null
                if (isSuccess) {
                    responseBody?.data?.let { authResponse ->
                        // Save tokens
                        preferencesManager.saveTokens(
                            authResponse.accessToken,
                            authResponse.refreshToken,
                            authResponse.user.email
                        )
                        Result.success(authResponse)
                    } ?: Result.failure(Exception("Response data is null"))
                } else {
                    val errorMessage = parseErrorMessage(response.errorBody()?.string())
                    Result.failure(Exception(errorMessage))
                }
            } else {
                val errorMessage = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

