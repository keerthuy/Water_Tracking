package com.healthtrack.app.ui.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.healthtrack.app.ui.model.HydrationLog
import com.healthtrack.app.ui.model.MedicationItem
import com.healthtrack.app.ui.model.UserProfile
import com.healthtrack.app.ui.components.DailyAdherenceCard
import com.healthtrack.app.ui.navigation.MainTab
import com.healthtrack.app.ui.theme.HealthNexaCard
import com.healthtrack.app.ui.theme.RiskLowGreen
import com.healthtrack.app.ui.theme.RiskLowGreenBg
import com.healthtrack.app.ui.theme.RiskModerateAmber
import com.healthtrack.app.ui.theme.RiskModerateAmberBg
import com.healthtrack.app.ui.theme.WaterBluePrimary
import java.util.Locale

@Composable
fun HomeScreen(
    userProfile: UserProfile?,
    hydrationLogs: List<HydrationLog>,
    medications: List<MedicationItem>,
    onNavigateTab: (MainTab) -> Unit,
    onNavigateToAddMedication: () -> Unit
) {
    val totalWaterMl = hydrationLogs.sumOf { it.amountMl }
    val waterTargetMl = userProfile?.dailyWaterTarget ?: 2500
    val waterProgress = (totalWaterMl.toFloat() / waterTargetMl.toFloat()).coerceIn(0f, 1f)

    val takenMeds = medications.count { it.isTaken }
    val totalMeds = medications.size

    val formattedName = remember(userProfile?.name) {
        val raw = userProfile?.name ?: "User"
        raw.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        }
    }

    // Computed Status (U6)
    val isOnTrack = waterProgress >= 0.5f || (totalMeds > 0 && takenMeds > 0) || totalMeds == 0
    val statusText = if (isOnTrack) "On Track" else "Needs Attention"
    val statusBg = if (isOnTrack) RiskLowGreenBg else RiskModerateAmberBg
    val statusFg = if (isOnTrack) RiskLowGreen else RiskModerateAmber
    val statusIcon = if (isOnTrack) Icons.Rounded.CheckCircle else Icons.Rounded.Info

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header & Dynamic Computed Status Badge (U6 & L8)
        HealthNexaCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hello, $formattedName",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Adherence Streak: 🔥 ${userProfile?.adherenceStreak ?: 0} Days",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Dynamic Status Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusBg,
                    border = BorderStroke(1.dp, statusFg.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = statusText,
                            tint = statusFg,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = statusFg
                        )
                    }
                }
            }
        }

        // Quick Action Banner - Rule-Based Ingredient Safety Scanner Card
        HealthNexaCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onNavigateTab(MainTab.EVALUATE) },
            backgroundColor = MaterialTheme.colorScheme.primary,
            borderColor = MaterialTheme.colorScheme.primary
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.QrCodeScanner,
                            contentDescription = "Evaluate Ingredient",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Rule-Based Ingredient Safety Scanner",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Check whether food ingredients & nutrients are safe for your active health conditions",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Hydration Progress Widget
        HealthNexaCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onNavigateTab(MainTab.WATER) }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.WaterDrop,
                        contentDescription = null,
                        tint = WaterBluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Hydration Tracker",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Text(
                    text = "$totalWaterMl / $waterTargetMl ml",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = WaterBluePrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { waterProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape),
                color = WaterBluePrimary,
                trackColor = WaterBluePrimary.copy(alpha = 0.15f)
            )
        }

        // Medication Overview / Daily Adherence Visualizer Widget
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Medication Schedule",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(onClick = onNavigateToAddMedication) {
                    Text(
                        text = "+ Add Medication",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            DailyAdherenceCard(
                medications = medications,
                onClick = { onNavigateTab(MainTab.MEDS) }
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    com.healthtrack.app.ui.theme.HealthNexaTheme {
        HomeScreen(
            userProfile = UserProfile(
                name = "Alex Morgan",
                email = "alex.morgan@healthnexa.io",
                weight = 72.0,
                dailyWaterTarget = 2500,
                adherenceStreak = 7,
                healthConditions = listOf("Hypertension", "Sodium Sensitivity")
            ),
            hydrationLogs = listOf(
                HydrationLog(amountMl = 250, label = "Water"),
                HydrationLog(amountMl = 500, label = "Water")
            ),
            medications = listOf(
                MedicationItem(name = "Metformin XR", dosage = "500 mg", timing = "8:00 AM", isTaken = true),
                MedicationItem(name = "Lisinopril", dosage = "10 mg", timing = "6:00 PM", isTaken = false)
            ),
            onNavigateTab = {},
            onNavigateToAddMedication = {}
        )
    }
}

