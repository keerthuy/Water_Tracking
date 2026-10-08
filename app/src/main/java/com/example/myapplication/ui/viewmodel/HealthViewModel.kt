package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.AuthState
import com.example.myapplication.data.model.HydrationLog
import com.example.myapplication.data.model.IngredientEvaluation
import com.example.myapplication.data.model.MedicationItem
import com.example.myapplication.data.model.UserProfile
import com.example.myapplication.data.repository.HealthRepository
import com.example.myapplication.data.repository.HealthRepositoryImpl
import com.example.myapplication.ui.navigation.MainTab
import com.example.myapplication.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HealthViewModel(
    val repository: HealthRepository = HealthRepositoryImpl()
) : ViewModel() {

    private val navStack = mutableListOf<Screen>(Screen.Splash)

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Splash)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _currentMainTab = MutableStateFlow(MainTab.HOME)
    val currentMainTab: StateFlow<MainTab> = _currentMainTab.asStateFlow()

    val authState: StateFlow<AuthState> = repository.authState
        .stateIn(viewModelScope, SharingStarted.Eagerly, AuthState.Unauthenticated)

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val hydrationLogs: StateFlow<List<HydrationLog>> = repository.hydrationLogs
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val medications: StateFlow<List<MedicationItem>> = repository.medications
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val evaluations: StateFlow<List<IngredientEvaluation>> = repository.evaluations
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _lastEvaluation = MutableStateFlow<IngredientEvaluation?>(null)
    val lastEvaluation: StateFlow<IngredientEvaluation?> = _lastEvaluation.asStateFlow()

    fun navigateTo(screen: Screen, clearStack: Boolean = false) {
        if (clearStack) {
            navStack.clear()
        }
        navStack.add(screen)
        _currentScreen.value = screen
    }

    fun popBackStack(): Boolean {
        if (navStack.size > 1) {
            navStack.removeAt(navStack.size - 1)
            _currentScreen.value = navStack.last()
            return true
        }
        return false
    }

    fun selectMainTab(tab: MainTab) {
        _currentMainTab.value = tab
        if (_currentScreen.value !is Screen.Main) {
            navigateTo(Screen.Main(tab.routeKey))
        }
    }

    fun handleSplashFinished() {
        if (authState.value is AuthState.Authenticated) {
            navigateTo(Screen.Main(), clearStack = true)
        } else {
            navigateTo(Screen.Onboarding, clearStack = true)
        }
    }

    fun signIn(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.signIn(email, pass)
            if (result.isSuccess) {
                navigateTo(Screen.Main(), clearStack = true)
                onSuccess()
            } else {
                onError(result.exceptionOrNull()?.message ?: "Sign in failed")
            }
        }
    }

    fun signUp(
        name: String,
        email: String,
        pass: String,
        weight: Double,
        conditions: List<String>,
        hipaaConsent: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.signUp(
                name = name,
                email = email,
                password = pass,
                weight = weight,
                healthConditions = conditions,
                hipaaConsentAccepted = hipaaConsent
            )
            if (result.isSuccess) {
                navigateTo(Screen.Main(), clearStack = true)
                onSuccess()
            } else {
                onError(result.exceptionOrNull()?.message ?: "Sign up failed")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            navigateTo(Screen.Onboarding, clearStack = true)
        }
    }

    fun addHydrationLog(amountMl: Int, label: String) {
        viewModelScope.launch {
            repository.addHydrationLog(amountMl, label)
        }
    }

    fun restoreHydrationLog(log: HydrationLog) {
        viewModelScope.launch {
            repository.restoreHydrationLog(log)
        }
    }

    fun deleteHydrationLog(id: String) {
        viewModelScope.launch {
            repository.deleteHydrationLog(id)
        }
    }

    fun addMedication(medication: MedicationItem) {
        viewModelScope.launch {
            repository.addMedication(medication)
            popBackStack()
        }
    }

    fun addMedication(name: String, dosage: String, timing: String, frequency: String, formFactor: String) {
        viewModelScope.launch {
            val newItem = MedicationItem(
                name = name,
                dosage = dosage,
                timing = timing,
                frequency = frequency,
                formFactor = formFactor,
                isTaken = false,
                nextDoseTime = "Today, $timing"
            )
            repository.addMedication(newItem)
            popBackStack()
        }
    }

    fun updateMedication(medication: MedicationItem) {
        viewModelScope.launch {
            repository.updateMedication(medication)
            popBackStack()
        }
    }

    fun toggleMedicationTaken(id: String) {
        viewModelScope.launch {
            repository.toggleMedicationTaken(id)
        }
    }

    fun deleteMedication(id: String) {
        viewModelScope.launch {
            repository.deleteMedication(id)
        }
    }

    fun evaluateIngredient(name: String, amount: Double, unit: String, condition: String) {
        viewModelScope.launch {
            val eval = repository.evaluateIngredient(name, amount, unit, condition)
            _lastEvaluation.value = eval
            navigateTo(
                Screen.EvaluationResult(
                    ingredientName = eval.name,
                    amount = eval.amount,
                    unit = eval.unit,
                    condition = eval.healthCondition,
                    riskLevel = eval.riskLevel.name,
                    recommendation = eval.recommendation
                )
            )
        }
    }
}
