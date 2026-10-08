package com.example.myapplication.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Splash : Screen

    @Serializable
    data object Onboarding : Screen

    @Serializable
    data object SignIn : Screen

    @Serializable
    data object SignUp : Screen

    @Serializable
    data class Main(val initialTab: String = "home") : Screen

    @Serializable
    data class AddMedication(val editMedicationId: String? = null) : Screen

    @Serializable
    data class EvaluationResult(
        val ingredientName: String = "",
        val amount: Double = 0.0,
        val unit: String = "mg",
        val condition: String = "",
        val riskLevel: String = "LOW",
        val recommendation: String = ""
    ) : Screen
}

enum class MainTab(val routeKey: String, val title: String) {
    HOME("home", "Home"),
    WATER("water", "Hydration"),
    MEDS("meds", "Medications"),
    EVALUATE("evaluate", "Evaluate"),
    PROFILE("profile", "Profile")
}
