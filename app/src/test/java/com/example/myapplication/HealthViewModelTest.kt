package com.example.myapplication

import com.example.myapplication.data.repository.HealthRepositoryImpl
import com.example.myapplication.ui.navigation.MainTab
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.viewmodel.HealthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HealthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: HealthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HealthViewModel(HealthRepositoryImpl())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialScreenIsSplash() {
        assertEquals(Screen.Splash, viewModel.currentScreen.value)
    }

    @Test
    fun testSplashFinishedUnauthenticatedNavigatesToOnboarding() {
        viewModel.handleSplashFinished()
        assertEquals(Screen.Onboarding, viewModel.currentScreen.value)
    }

    @Test
    fun testNavigationStackAndPop() {
        viewModel.navigateTo(Screen.SignIn)
        assertEquals(Screen.SignIn, viewModel.currentScreen.value)

        val popped = viewModel.popBackStack()
        assertTrue(popped)
        assertEquals(Screen.Splash, viewModel.currentScreen.value)
    }

    @Test
    fun testSignInFlowNavigatesToMain() = runTest {
        viewModel.signIn("alex.morgan@healthnexa.io", "password123", onSuccess = {}, onError = {})
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.currentScreen.value is Screen.Main)
    }

    @Test
    fun testSignUpFlowNavigatesToMain() = runTest {
        viewModel.signUp(
            name = "Jane Doe",
            email = "jane@healthnexa.io",
            pass = "password123",
            weight = 65.0,
            conditions = listOf("Hypertension"),
            hipaaConsent = true,
            onSuccess = {},
            onError = {}
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.currentScreen.value is Screen.Main)
        assertEquals("Jane Doe", viewModel.userProfile.value?.name)
    }

    @Test
    fun testSelectMainTab() {
        viewModel.selectMainTab(MainTab.WATER)
        assertEquals(MainTab.WATER, viewModel.currentMainTab.value)
        assertTrue(viewModel.currentScreen.value is Screen.Main)
    }

    @Test
    fun testEvaluateIngredientNavigatesToResultScreen() = runTest {
        viewModel.evaluateIngredient("Sodium", 3500.0, "mg", "Hypertension")
        testDispatcher.scheduler.advanceUntilIdle()

        val screen = viewModel.currentScreen.value
        assertTrue(screen is Screen.EvaluationResult)
        if (screen is Screen.EvaluationResult) {
            assertEquals("Sodium", screen.ingredientName)
            assertEquals("CRITICAL", screen.riskLevel)
        }
    }

    @Test
    fun testAddAndDeleteHydrationLog() = runTest {
        val initialCount = viewModel.hydrationLogs.value.size
        viewModel.addHydrationLog(350, "Green Tea")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(initialCount + 1, viewModel.hydrationLogs.value.size)
        assertEquals("Green Tea", viewModel.hydrationLogs.value.first().label)

        val logId = viewModel.hydrationLogs.value.first().id
        viewModel.deleteHydrationLog(logId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(initialCount, viewModel.hydrationLogs.value.size)
    }

    @Test
    fun testToggleMedicationTaken() = runTest {
        val initialMed = viewModel.medications.value.first()
        val initialTaken = initialMed.isTaken

        viewModel.toggleMedicationTaken(initialMed.id)
        testDispatcher.scheduler.advanceUntilIdle()

        val updatedMed = viewModel.medications.value.first { it.id == initialMed.id }
        assertEquals(!initialTaken, updatedMed.isTaken)
    }

    @Test
    fun testSignOutNavigatesToOnboarding() = runTest {
        viewModel.signIn("alex.morgan@healthnexa.io", "password123", onSuccess = {}, onError = {})
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.currentScreen.value is Screen.Main)

        viewModel.signOut()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(Screen.Onboarding, viewModel.currentScreen.value)
    }
}
