package com.example.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HydrationLog(
    val id: String = java.util.UUID.randomUUID().toString(),
    val amountMl: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val label: String = "Water"
)
