package com.healthtrack.app.ui.navigation

object NavRoutes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val SIGN_IN = "signin"
    const val SIGN_UP = "signup"
    const val MAIN = "main"
    const val ADD_MEDICATION = "add_medication"
    const val EVALUATION_RESULT = "evaluation_result"
}

sealed interface Screen {
    data object Splash : Screen
    data object Onboarding : Screen
    data object SignIn : Screen
    data object SignUp : Screen
    data class Main(val initialTab: String = "home") : Screen
    data class AddMedication(val editMedicationId: String? = null) : Screen
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
