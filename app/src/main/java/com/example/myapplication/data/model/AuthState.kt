package com.example.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthState {
    @Serializable
    data object Unauthenticated : AuthState()

    @Serializable
    data object Loading : AuthState()

    @Serializable
    data class Authenticated(val userProfile: UserProfile) : AuthState()

    @Serializable
    data class Error(val message: String) : AuthState()
}
