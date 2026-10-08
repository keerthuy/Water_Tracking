package com.example.myapplication.data.repository

import com.example.myapplication.data.model.AuthState
import com.example.myapplication.data.model.HydrationLog
import com.example.myapplication.data.model.IngredientEvaluation
import com.example.myapplication.data.model.MedicationItem
import com.example.myapplication.data.model.UserProfile
import kotlinx.coroutines.flow.StateFlow

interface HealthRepository {
    val authState: StateFlow<AuthState>
    val userProfile: StateFlow<UserProfile?>
    val hydrationLogs: StateFlow<List<HydrationLog>>
    val medications: StateFlow<List<MedicationItem>>
    val evaluations: StateFlow<List<IngredientEvaluation>>

    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        weight: Double,
        healthConditions: List<String>,
        hipaaConsentAccepted: Boolean
    ): Result<UserProfile>

    suspend fun signIn(email: String, password: String): Result<UserProfile>

    suspend fun signOut()

    suspend fun addHydrationLog(amountMl: Int, label: String = "Water")
    suspend fun restoreHydrationLog(log: HydrationLog)
    suspend fun deleteHydrationLog(id: String)

    suspend fun addMedication(medication: MedicationItem)
    suspend fun updateMedication(medication: MedicationItem)
    suspend fun toggleMedicationTaken(id: String)
    suspend fun deleteMedication(id: String)

    suspend fun evaluateIngredient(
        name: String,
        amount: Double,
        unit: String,
        condition: String
    ): IngredientEvaluation

    suspend fun addEvaluation(evaluation: IngredientEvaluation)

    suspend fun updateUserProfile(profile: UserProfile)
}
