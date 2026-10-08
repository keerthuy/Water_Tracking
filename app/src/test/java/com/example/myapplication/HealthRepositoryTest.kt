package com.example.myapplication

import com.example.myapplication.data.model.AuthState
import com.example.myapplication.data.model.MedicationItem
import com.example.myapplication.data.model.RiskLevel
import com.example.myapplication.data.repository.HealthRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HealthRepositoryTest {

    private lateinit var repository: HealthRepositoryImpl

    @Before
    fun setUp() {
        repository = HealthRepositoryImpl()
    }

    @Test
    fun testInitialState() {
        assertEquals(AuthState.Unauthenticated, repository.authState.value)
        assertTrue(repository.hydrationLogs.value.isNotEmpty())
        assertTrue(repository.medications.value.isNotEmpty())
        assertTrue(repository.evaluations.value.isNotEmpty())
    }

    @Test
    fun testSignInSuccess() = runTest {
        val result = repository.signIn("alex.morgan@healthnexa.io", "password123")
        assertTrue(result.isSuccess)
        assertTrue(repository.authState.value is AuthState.Authenticated)
        assertNotNull(repository.userProfile.value)
        assertEquals("alex.morgan@healthnexa.io", repository.userProfile.value?.email)
    }

    @Test
    fun testSignUpSuccess() = runTest {
        val result = repository.signUp(
            name = "John Doe",
            email = "john@healthnexa.io",
            password = "securePassword",
            weight = 80.0,
            healthConditions = listOf("Hypertension"),
            hipaaConsentAccepted = true
        )
        assertTrue(result.isSuccess)
        assertEquals("John Doe", repository.userProfile.value?.name)
        assertEquals(80.0, repository.userProfile.value?.weight ?: 0.0, 0.01)
        assertTrue(repository.authState.value is AuthState.Authenticated)
    }

    @Test
    fun testAddHydrationLog() = runTest {
        val initialSize = repository.hydrationLogs.value.size
        repository.addHydrationLog(500, "After Workout Water")
        assertEquals(initialSize + 1, repository.hydrationLogs.value.size)
        assertEquals(500, repository.hydrationLogs.value.first().amountMl)
        assertEquals("After Workout Water", repository.hydrationLogs.value.first().label)
    }

    @Test
    fun testToggleMedicationTaken() = runTest {
        val firstMed = repository.medications.value.first()
        val initialTaken = firstMed.isTaken
        repository.toggleMedicationTaken(firstMed.id)

        val updatedMed = repository.medications.value.first { it.id == firstMed.id }
        assertEquals(!initialTaken, updatedMed.isTaken)
    }

    @Test
    fun testEvaluateIngredient() = runTest {
        val evalCriticalSodium = repository.evaluateIngredient("Sodium", 3500.0, "mg", "Hypertension")
        assertEquals(RiskLevel.CRITICAL, evalCriticalSodium.riskLevel)
        assertTrue(evalCriticalSodium.ratio >= 1.5)

        val evalHighSodium = repository.evaluateIngredient("Sodium", 2200.0, "mg", "Hypertension")
        assertEquals(RiskLevel.HIGH, evalHighSodium.riskLevel)

        val evalLowSodium = repository.evaluateIngredient("Sodium", 500.0, "mg", "Hypertension")
        assertEquals(RiskLevel.LOW, evalLowSodium.riskLevel)
    }
}
