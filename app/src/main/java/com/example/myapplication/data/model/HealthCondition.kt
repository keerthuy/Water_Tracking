package com.example.myapplication.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HealthCondition(
    val id: String,
    val name: String,
    val category: String,
    val description: String = "",
    val restrictedIngredients: List<String> = emptyList()
)
