package com.healthtrack.app

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import com.healthtrack.app.ui.navigation.HealthTrackApp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.LEGACY)
@Config(sdk = [33])
class SmokeComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testAppRendersWelcomeText() {
        composeTestRule.setContent {
            val app = ApplicationProvider.getApplicationContext<HealthTrackApplication>()
            if (FirebaseApp.getApps(app).isEmpty()) {
                FirebaseApp.initializeApp(app)
            }
            HealthTrackApp()
        }
        
        composeTestRule.onNodeWithText("HEALTHNEXA").assertIsDisplayed()
    }
}
