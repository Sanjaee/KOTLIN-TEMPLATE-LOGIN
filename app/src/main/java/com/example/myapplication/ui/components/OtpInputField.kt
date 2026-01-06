package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpInputField(
    otpText: String,
    onOtpTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    otpCount: Int = 6
) {
    val focusRequesters = remember { List(otpCount) { FocusRequester() } }
    
    // Initialize focus to first field
    LaunchedEffect(Unit) {
        if (otpText.isEmpty()) {
            focusRequesters[0].requestFocus()
        } else {
            // Focus on next empty field or last field
            val nextIndex = otpText.length.coerceAtMost(otpCount - 1)
            focusRequesters[nextIndex].requestFocus()
        }
    }
    
    // Sync focus when otpText changes externally
    LaunchedEffect(otpText) {
        val nextIndex = otpText.length.coerceAtMost(otpCount - 1)
        if (otpText.length < otpCount) {
            focusRequesters[nextIndex].requestFocus()
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(otpCount) { index ->
            val isFilled = index < otpText.length
            val currentChar = if (index < otpText.length) otpText[index].toString() else ""
            val currentValue = TextFieldValue(
                text = currentChar,
                selection = TextRange(currentChar.length)
            )
            
            OutlinedTextField(
                value = currentValue,
                onValueChange = { newValue ->
                    val inputText = newValue.text.filter { it.isDigit() }
                    
                    if (inputText.isEmpty()) {
                        // Handle backspace/delete
                        if (index < otpText.length) {
                            // Remove digit at current position
                            val newOtpText = otpText.substring(0, index) + otpText.substring(index + 1)
                            onOtpTextChange(newOtpText)
                            
                            // Move to previous field if not first
                            if (index > 0) {
                                focusRequesters[index - 1].requestFocus()
                            }
                        } else if (otpText.isNotEmpty()) {
                            // Remove last digit if deleting from empty field
                            val newOtpText = otpText.dropLast(1)
                            onOtpTextChange(newOtpText)
                            if (index > 0) {
                                focusRequesters[index - 1].requestFocus()
                            }
                        }
                    } else {
                        // Handle input (including paste)
                        var digitsToAdd = inputText
                        
                        // If paste detected (multiple digits), process all
                        if (inputText.length > 1) {
                            // Paste detected - fill remaining boxes
                            val remainingSlots = otpCount - index
                            digitsToAdd = inputText.take(remainingSlots)
                            
                            // Build new OTP text
                            val newOtpText = otpText.substring(0, index) + digitsToAdd
                            val finalOtpText = newOtpText.take(otpCount)
                            
                            onOtpTextChange(finalOtpText)
                            
                            // Focus on the next empty field or last field
                            val nextIndex = finalOtpText.length.coerceAtMost(otpCount - 1)
                            if (nextIndex < otpCount) {
                                focusRequesters[nextIndex].requestFocus()
                            }
                        } else {
                            // Single digit input
                            val char = inputText[0]
                            
                            // Update OTP at this position
                            val newOtpText = if (index < otpText.length) {
                                // Replace existing digit
                                otpText.substring(0, index) + char + otpText.substring(index + 1)
                            } else {
                                // Append new digit
                                otpText + char
                            }.take(otpCount)
                            
                            onOtpTextChange(newOtpText)
                            
                            // Move to next field if not last
                            if (index < otpCount - 1 && newOtpText.length > otpText.length) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .width(48.dp)
                    .height(56.dp)
                    .focusRequester(focusRequesters[index]),
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    autoCorrect = false
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Move to next field on done
                        if (index < otpCount - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                ),
                singleLine = true,
                enabled = enabled,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isFilled) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (isFilled)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.outline,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}

