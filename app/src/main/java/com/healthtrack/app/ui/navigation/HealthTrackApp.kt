package com.healthtrack.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.healthtrack.app.HealthTrackApplication
import com.healthtrack.app.ui.screens.auth.OnboardingScreen
import com.healthtrack.app.ui.screens.auth.SignInScreen
import com.healthtrack.app.ui.screens.auth.SignUpScreen
import com.healthtrack.app.ui.screens.auth.SplashScreen
import com.healthtrack.app.ui.screens.main.AddMedicationScreen
import com.healthtrack.app.ui.screens.main.EvaluationResultScreen
import com.healthtrack.app.ui.screens.main.MainTabScreen
import com.healthtrack.app.ui.screens.main.NotificationCenterDialog
import com.healthtrack.app.ui.viewmodel.HealthViewModel
import com.healthtrack.app.ui.viewmodel.ViewModelFactory

@Composable
fun HealthTrackApp() {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as HealthTrackApplication
    val container = app.container
    
    val viewModel: HealthViewModel = viewModel(factory = ViewModelFactory(container))

    val currentTab by viewModel.currentMainTab.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val hydrationLogs by viewModel.hydrationLogs.collectAsState()
    val medications by viewModel.medications.collectAsState()

    var showNotificationCenter by remember { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = NavRoutes.SPLASH) {
        composable(NavRoutes.SPLASH) {
            SplashScreen(onSplashFinished = { 
                if (userProfile != null) {
                    navController.navigate(NavRoutes.MAIN) { popUpTo(NavRoutes.SPLASH) { inclusive = true } }
                } else {
                    navController.navigate(NavRoutes.ONBOARDING) { popUpTo(NavRoutes.SPLASH) { inclusive = true } } 
                }
            })
        }
        composable(NavRoutes.ONBOARDING) {
            OnboardingScreen(
                onNavigateToSignIn = { navController.navigate(NavRoutes.SIGN_IN) },
                onNavigateToSignUp = { navController.navigate(NavRoutes.SIGN_UP) }
            )
        }
        composable(NavRoutes.SIGN_IN) {
            SignInScreen(
                onSignInClicked = { email, pass, onError -> 
                    viewModel.signIn(email, pass, { 
                        navController.navigate(NavRoutes.MAIN) { popUpTo(NavRoutes.SIGN_IN) { inclusive = true } } 
                    }, onError) 
                },
                onNavigateToSignUp = { navController.navigate(NavRoutes.SIGN_UP) }
            )
        }
        composable(NavRoutes.SIGN_UP) {
            SignUpScreen(
                onSignUpClicked = { name, email, pass, weight, conds, hipaa, onError -> 
                    viewModel.signUp(name, email, pass, weight, conds, hipaa, { 
                        navController.navigate(NavRoutes.MAIN) { popUpTo(NavRoutes.SIGN_UP) { inclusive = true } } 
                    }, onError) 
                },
                onNavigateToSignIn = { navController.navigate(NavRoutes.SIGN_IN) }
            )
        }
        composable(NavRoutes.MAIN) {
            MainTabScreen(
                currentTab = currentTab,
                userProfile = userProfile,
                hydrationLogs = hydrationLogs,
                medications = medications,
                onTabSelected = { viewModel.selectMainTab(it) },
                onNavigateToAddMedication = { navController.navigate(NavRoutes.ADD_MEDICATION) },
                onNavigateToEditMedication = { id -> navController.navigate("${NavRoutes.ADD_MEDICATION}?id=$id") },
                onAddHydration = { amount, label -> viewModel.addHydrationLog(amount, label) },
                onDeleteHydration = { id -> viewModel.deleteHydrationLog(id) },
                onToggleMedicationTaken = { id -> viewModel.toggleMedicationTaken(id) },
                onDeleteMedication = { id -> viewModel.deleteMedication(id) },
                onEvaluateIngredient = { name, amount, unit, cond -> 
                    viewModel.evaluateIngredient(name, amount, unit, cond) {
                        navController.navigate(NavRoutes.EVALUATION_RESULT)
                    } 
                },
                onSignOutClicked = { 
                    viewModel.signOut()
                    navController.navigate(NavRoutes.ONBOARDING) { popUpTo(NavRoutes.MAIN) { inclusive = true } } 
                },
                onOpenReminderSettings = { showNotificationCenter = true }
            )
        }
        composable(
            route = "${NavRoutes.ADD_MEDICATION}?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val medId = backStackEntry.arguments?.getString("id")
            val medToEdit = medications.find { it.id == medId }
            AddMedicationScreen(
                medicationToEdit = medToEdit,
                onBackClicked = { navController.popBackStack() },
                onSaveMedication = { item ->
                    viewModel.addMedication(item)
                    navController.popBackStack()
                }
            )
        }
        composable(NavRoutes.EVALUATION_RESULT) {
            val eval by viewModel.lastEvaluation.collectAsState()
            val currentEval = eval ?: Screen.EvaluationResult(
                ingredientName = "Sodium",
                amount = 1800.0,
                unit = "mg",
                condition = "Hypertension",
                riskLevel = "MODERATE",
                recommendation = "Moderate risk based on general daily intake."
            )
            EvaluationResultScreen(
                ingredientName = currentEval.ingredientName,
                amount = currentEval.amount,
                unit = currentEval.unit,
                condition = currentEval.condition,
                riskLevelStr = currentEval.riskLevel,
                recommendation = currentEval.recommendation,
                onBackClicked = { navController.popBackStack() }
            )
        }
    }

    if (showNotificationCenter) {
        NotificationCenterDialog(
            onDismiss = { showNotificationCenter = false },
            onSaveSettings = { interval, start, end, enabled, goal ->
                viewModel.updateProfile(
                    name = userProfile?.name ?: "User",
                    weightKg = (userProfile?.weight ?: 70.0).toFloat(),
                    waterGoalMl = goal,
                    goalIsManual = true
                )
            }
        )
    }
}
