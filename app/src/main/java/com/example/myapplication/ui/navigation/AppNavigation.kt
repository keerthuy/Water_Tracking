package com.example.myapplication.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.myapplication.ui.screens.auth.OnboardingScreen
import com.example.myapplication.ui.screens.auth.SignInScreen
import com.example.myapplication.ui.screens.auth.SignUpScreen
import com.example.myapplication.ui.screens.auth.SplashScreen
import com.example.myapplication.ui.screens.main.AddMedicationScreen
import com.example.myapplication.ui.screens.main.EvaluationResultScreen
import com.example.myapplication.ui.screens.main.MainTabScreen
import com.example.myapplication.ui.viewmodel.HealthViewModel

@Composable
fun AppNavigation(
    viewModel: HealthViewModel
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentTab by viewModel.currentMainTab.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val hydrationLogs by viewModel.hydrationLogs.collectAsState()
    val medications by viewModel.medications.collectAsState()

    BackHandler(enabled = currentScreen !is Screen.Splash && currentScreen !is Screen.Onboarding && currentScreen !is Screen.Main) {
        viewModel.popBackStack()
    }

    when (val screen = currentScreen) {
        is Screen.Splash -> {
            SplashScreen(
                onSplashFinished = { viewModel.handleSplashFinished() }
            )
        }

        is Screen.Onboarding -> {
            OnboardingScreen(
                onNavigateToSignIn = { viewModel.navigateTo(Screen.SignIn) },
                onNavigateToSignUp = { viewModel.navigateTo(Screen.SignUp) }
            )
        }

        is Screen.SignIn -> {
            SignInScreen(
                onSignInClicked = { email, pass, onError ->
                    viewModel.signIn(email, pass, onSuccess = {}, onError = onError)
                },
                onNavigateToSignUp = { viewModel.navigateTo(Screen.SignUp) }
            )
        }

        is Screen.SignUp -> {
            SignUpScreen(
                onSignUpClicked = { name, email, pass, weight, conditions, hipaaConsent, onError ->
                    viewModel.signUp(name, email, pass, weight, conditions, hipaaConsent, onSuccess = {}, onError = onError)
                },
                onNavigateToSignIn = { viewModel.navigateTo(Screen.SignIn) }
            )
        }

        is Screen.Main -> {
            MainTabScreen(
                currentTab = currentTab,
                userProfile = userProfile,
                hydrationLogs = hydrationLogs,
                medications = medications,
                onTabSelected = { viewModel.selectMainTab(it) },
                onNavigateToAddMedication = { viewModel.navigateTo(Screen.AddMedication()) },
                onNavigateToEditMedication = { id -> viewModel.navigateTo(Screen.AddMedication(editMedicationId = id)) },
                onAddHydration = { amount, label -> viewModel.addHydrationLog(amount, label) },
                onRestoreHydration = { log -> viewModel.restoreHydrationLog(log) },
                onDeleteHydration = { id -> viewModel.deleteHydrationLog(id) },
                onToggleMedicationTaken = { id -> viewModel.toggleMedicationTaken(id) },
                onDeleteMedication = { id -> viewModel.deleteMedication(id) },
                onEvaluateIngredient = { name, amount, unit, cond ->
                    viewModel.evaluateIngredient(name, amount, unit, cond)
                },
                onSignOutClicked = { viewModel.signOut() }
            )
        }

        is Screen.AddMedication -> {
            val medToEdit = medications.find { it.id == screen.editMedicationId }
            AddMedicationScreen(
                medicationToEdit = medToEdit,
                onBackClicked = { viewModel.popBackStack() },
                onSaveMedication = { item ->
                    if (screen.editMedicationId != null) {
                        viewModel.updateMedication(item)
                    } else {
                        viewModel.addMedication(item)
                    }
                }
            )
        }

        is Screen.EvaluationResult -> {
            EvaluationResultScreen(
                ingredientName = screen.ingredientName,
                amount = screen.amount,
                unit = screen.unit,
                condition = screen.condition,
                riskLevelStr = screen.riskLevel,
                recommendation = screen.recommendation,
                onBackClicked = { viewModel.popBackStack() }
            )
        }
    }
}
