package com.example.myapplication.data.repository

import com.example.myapplication.data.model.AuthState
import com.example.myapplication.data.model.DoseTime
import com.example.myapplication.data.model.HydrationLog
import com.example.myapplication.data.model.IngredientEvaluation
import com.example.myapplication.data.model.MedicationHistoryEntry
import com.example.myapplication.data.model.MedicationItem
import com.example.myapplication.data.model.RiskLevel
import com.example.myapplication.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class HealthRepositoryImpl : HealthRepository {

    private val defaultUser = UserProfile(
        id = "user_001",
        name = "Alex Morgan",
        email = "alex.morgan@healthnexa.io",
        weight = 72.5,
        dailyWaterTarget = 2500,
        riskTier = "Low Risk",
        adherenceStreak = 8,
        healthConditions = listOf("Hypertension", "Mild Sodium Sensitivity"),
        hipaaConsentAccepted = true
    )

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    override val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _hydrationLogs = MutableStateFlow<List<HydrationLog>>(
        listOf(
            HydrationLog(amountMl = 350, label = "Morning Water", timestamp = System.currentTimeMillis() - 14400000),
            HydrationLog(amountMl = 500, label = "Post Gym Electrolytes", timestamp = System.currentTimeMillis() - 10800000),
            HydrationLog(amountMl = 250, label = "Green Tea", timestamp = System.currentTimeMillis() - 3600000)
        )
    )
    override val hydrationLogs: StateFlow<List<HydrationLog>> = _hydrationLogs.asStateFlow()

    private val _medications = MutableStateFlow<List<MedicationItem>>(
        listOf(
            MedicationItem(
                id = "med_1",
                name = "Metformin",
                dosage = "500 mg",
                timing = "8:00 AM",
                frequency = "Twice Daily",
                formFactor = "Pill/Tablet",
                isTaken = true,
                nextDoseTime = "Today, 6:00 PM",
                strength = "500",
                unit = "mg",
                doseTimes = listOf(
                    DoseTime("8:00 AM", "Take with breakfast"),
                    DoseTime("6:00 PM", "Take with dinner")
                ),
                activeDays = listOf("M", "T", "W", "T", "F", "S", "S"),
                refillReminder = true,
                stockCount = 28,
                specialInstructions = "Take with meals to minimize gastrointestinal distress.",
                history = listOf(
                    MedicationHistoryEntry(
                        timestamp = System.currentTimeMillis() - 14400000,
                        action = "Taken",
                        note = "Taken with breakfast"
                    )
                )
            ),
            MedicationItem(
                id = "med_2",
                name = "Lisinopril",
                dosage = "10 mg",
                timing = "9:00 AM",
                frequency = "Once Daily",
                formFactor = "Pill/Tablet",
                isTaken = true,
                nextDoseTime = "Tomorrow, 9:00 AM",
                strength = "10",
                unit = "mg",
                doseTimes = listOf(
                    DoseTime("9:00 AM", "Take with breakfast")
                ),
                activeDays = listOf("M", "T", "W", "T", "F", "S", "S"),
                refillReminder = true,
                stockCount = 20,
                specialInstructions = "Take consistently in the morning.",
                history = listOf(
                    MedicationHistoryEntry(
                        timestamp = System.currentTimeMillis() - 10800000,
                        action = "Taken",
                        note = "Taken on schedule"
                    )
                )
            ),
            MedicationItem(
                id = "med_3",
                name = "Vitamin D",
                dosage = "1000 IU",
                timing = "1:00 PM",
                frequency = "Once Daily",
                formFactor = "Capsule",
                isTaken = false,
                nextDoseTime = "Today, 1:00 PM",
                strength = "1000",
                unit = "IU",
                doseTimes = listOf(
                    DoseTime("1:00 PM", "Take with lunch")
                ),
                activeDays = listOf("M", "T", "W", "T", "F", "S", "S"),
                refillReminder = true,
                stockCount = 45,
                specialInstructions = "Take with a fat-containing meal for optimal absorption.",
                history = emptyList()
            ),
            MedicationItem(
                id = "med_4",
                name = "Omega-3",
                dosage = "1000 mg",
                timing = "8:00 PM",
                frequency = "Once Daily",
                formFactor = "Capsule",
                isTaken = false,
                nextDoseTime = "Today, 8:00 PM",
                strength = "1000",
                unit = "mg",
                doseTimes = listOf(
                    DoseTime("8:00 PM", "Take with dinner")
                ),
                activeDays = listOf("M", "T", "W", "T", "F", "S", "S"),
                refillReminder = true,
                stockCount = 50,
                specialInstructions = "Swallow whole with plenty of liquid.",
                history = emptyList()
            )
        )
    )
    override val medications: StateFlow<List<MedicationItem>> = _medications.asStateFlow()

    private val _evaluations = MutableStateFlow<List<IngredientEvaluation>>(
        listOf(
            IngredientEvaluation(
                id = "eval_1",
                name = "Sodium",
                amount = 1800.0,
                unit = "mg",
                healthCondition = "Hypertension",
                limit = 2000.0,
                ratio = 0.9,
                riskLevel = RiskLevel.MODERATE,
                recommendation = "Approaching daily threshold limit of 2000mg for Hypertension condition."
            ),
            IngredientEvaluation(
                id = "eval_2",
                name = "Added Sugar",
                amount = 45.0,
                unit = "g",
                healthCondition = "Type 2 Diabetes",
                limit = 25.0,
                ratio = 1.8,
                riskLevel = RiskLevel.HIGH,
                recommendation = "Exceeds daily threshold limit of 25g. Consider low-sugar alternatives."
            )
        )
    )
    override val evaluations: StateFlow<List<IngredientEvaluation>> = _evaluations.asStateFlow()

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
        weight: Double,
        healthConditions: List<String>,
        hipaaConsentAccepted: Boolean
    ): Result<UserProfile> {
        return if (email.isNotEmpty() && password.length >= 6 && hipaaConsentAccepted) {
            val newUser = UserProfile(
                id = UUID.randomUUID().toString(),
                name = name.ifEmpty { "HealthNexa User" },
                email = email,
                weight = if (weight > 0) weight else 70.0,
                dailyWaterTarget = ((if (weight > 0) weight else 70.0) * 35).toInt().coerceAtLeast(2000),
                riskTier = if (healthConditions.size > 2) "Moderate Risk" else "Low Risk",
                adherenceStreak = 1,
                healthConditions = healthConditions.ifEmpty { listOf("General Wellness") },
                hipaaConsentAccepted = hipaaConsentAccepted
            )
            _userProfile.value = newUser
            _authState.value = AuthState.Authenticated(newUser)
            Result.success(newUser)
        } else {
            val errMsg = if (!hipaaConsentAccepted) "HIPAA consent is required." else "Invalid credentials provided."
            _authState.value = AuthState.Error(errMsg)
            Result.failure(IllegalArgumentException(errMsg))
        }
    }

    override suspend fun signIn(email: String, password: String): Result<UserProfile> {
        return if (email.contains("@") && password.length >= 4) {
            val loggedInUser = defaultUser.copy(email = email)
            _userProfile.value = loggedInUser
            _authState.value = AuthState.Authenticated(loggedInUser)
            Result.success(loggedInUser)
        } else {
            val errMsg = "Invalid email or password"
            _authState.value = AuthState.Error(errMsg)
            Result.failure(IllegalArgumentException(errMsg))
        }
    }

    override suspend fun signOut() {
        _userProfile.value = null
        _authState.value = AuthState.Unauthenticated
    }

    override suspend fun addHydrationLog(amountMl: Int, label: String) {
        val newLog = HydrationLog(
            amountMl = amountMl,
            label = label.ifEmpty { "Water" },
            timestamp = System.currentTimeMillis()
        )
        _hydrationLogs.value = listOf(newLog) + _hydrationLogs.value
    }

    override suspend fun restoreHydrationLog(log: HydrationLog) {
        if (_hydrationLogs.value.none { it.id == log.id }) {
            _hydrationLogs.value = listOf(log) + _hydrationLogs.value
        }
    }

    override suspend fun deleteHydrationLog(id: String) {
        _hydrationLogs.value = _hydrationLogs.value.filterNot { it.id == id }
    }

    override suspend fun addMedication(medication: MedicationItem) {
        _medications.value = _medications.value + medication
    }

    override suspend fun updateMedication(medication: MedicationItem) {
        _medications.value = _medications.value.map { med ->
            if (med.id == medication.id) medication else med
        }
    }

    override suspend fun toggleMedicationTaken(id: String) {
        _medications.value = _medications.value.map { med ->
            if (med.id == id) {
                val nextTaken = !med.isTaken
                val action = if (nextTaken) "Taken" else "Unmarked"
                val newEntry = MedicationHistoryEntry(
                    timestamp = System.currentTimeMillis(),
                    action = action,
                    note = if (nextTaken) "Marked as taken" else "Marked as not taken"
                )
                val updatedStock = if (nextTaken && med.stockCount > 0) med.stockCount - 1 else med.stockCount
                med.copy(
                    isTaken = nextTaken,
                    stockCount = updatedStock,
                    nextDoseTime = if (nextTaken) "Completed for today" else "Today, ${med.timing}",
                    history = listOf(newEntry) + med.history
                )
            } else med
        }
        updateAdherenceStreak()
    }

    override suspend fun deleteMedication(id: String) {
        _medications.value = _medications.value.filterNot { it.id == id }
    }

    override suspend fun evaluateIngredient(
        name: String,
        amount: Double,
        unit: String,
        condition: String
    ): IngredientEvaluation {
        val limit = when (name.lowercase()) {
            "sodium", "salt" -> 2000.0
            "sugar", "added sugar" -> 25.0
            "caffeine" -> 200.0
            "potassium" -> 3000.0
            "saturated fat" -> 15.0
            else -> 100.0
        }

        val ratio = amount / limit
        val riskLevel = when {
            ratio >= 1.5 -> RiskLevel.CRITICAL
            ratio >= 1.0 -> RiskLevel.HIGH
            ratio >= 0.7 -> RiskLevel.MODERATE
            else -> RiskLevel.LOW
        }

        val recommendation = when (riskLevel) {
            RiskLevel.CRITICAL -> "DANGER: Ingredient level ($amount $unit) severely exceeds recommended safety threshold ($limit $unit) for $condition."
            RiskLevel.HIGH -> "WARNING: Ingredient level ($amount $unit) exceeds safety limit ($limit $unit) for $condition. Moderation strongly advised."
            RiskLevel.MODERATE -> "CAUTION: Ingredient level ($amount $unit) is close to maximum daily threshold ($limit $unit) for $condition."
            RiskLevel.LOW -> "SAFE: Ingredient level ($amount $unit) is well within normal safe parameters ($limit $unit) for $condition."
        }

        val evaluation = IngredientEvaluation(
            name = name,
            amount = amount,
            unit = unit,
            healthCondition = condition,
            limit = limit,
            ratio = ratio,
            riskLevel = riskLevel,
            recommendation = recommendation
        )

        addEvaluation(evaluation)
        return evaluation
    }

    override suspend fun addEvaluation(evaluation: IngredientEvaluation) {
        _evaluations.value = listOf(evaluation) + _evaluations.value
    }

    override suspend fun updateUserProfile(profile: UserProfile) {
        _userProfile.value = profile
        if (_authState.value is AuthState.Authenticated) {
            _authState.value = AuthState.Authenticated(profile)
        }
    }

    private fun updateAdherenceStreak() {
        val currentProfile = _userProfile.value ?: return
        val takenCount = _medications.value.count { it.isTaken }
        val totalCount = _medications.value.size
        if (totalCount > 0 && takenCount == totalCount) {
            val updated = currentProfile.copy(adherenceStreak = currentProfile.adherenceStreak + 1)
            _userProfile.value = updated
        }
    }
}
