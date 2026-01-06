package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean? = null,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("data")
    val data: T? = null
)

data class RegisterResponse(
    @SerializedName("message")
    val message: String,
    
    @SerializedName("user")
    val user: User,
    
    @SerializedName("requires_verification")
    val requiresVerification: Boolean,
    
    @SerializedName("verification_token")
    val verificationToken: String? = null
)

data class AuthResponse(
    @SerializedName("user")
    val user: User,
    
    @SerializedName("access_token")
    val accessToken: String,
    
    @SerializedName("refresh_token")
    val refreshToken: String,
    
    @SerializedName("expires_in")
    val expiresIn: Int
)

data class ErrorResponse(
    @SerializedName("success")
    val success: Boolean = false,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: Map<String, Any>? = null
)

