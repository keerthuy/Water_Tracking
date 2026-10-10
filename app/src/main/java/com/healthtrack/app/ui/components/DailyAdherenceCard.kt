package com.healthtrack.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.healthtrack.app.ui.model.MedicationItem

@Composable
fun DailyAdherenceCard(
    medications: List<MedicationItem>,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val takenCount = medications.count { it.isTaken }
    val totalCount = medications.size
    val adherencePercent = if (totalCount > 0) (takenCount * 100) / totalCount else 0

    val cardShape = RoundedCornerShape(16.dp)
    val cardModifier = if (onClick != null) {
        modifier
            .clip(cardShape)
            .clickable { onClick() }
    } else {
        modifier
    }

    Surface(
        modifier = cardModifier.fillMaxWidth(),
        color = Color(0xFFE8F5F1),
        shape = cardShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFC8E6C9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalPharmacy,
                            contentDescription = "Daily Adherence Icon",
                            tint = Color(0xFF003731),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Daily Adherence",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF003731),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "$takenCount of $totalCount taken today",
                            fontSize = 12.sp,
                            color = Color(0xFF557973)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$adherencePercent%",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF006A60)
                )
            }

            // Progress Bar
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { if (totalCount > 0) takenCount.toFloat() / totalCount else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = Color(0xFF006A60),
                trackColor = Color(0xFFD0E7E2)
            )

            // Medication Dot Indicators Row
            if (medications.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                @OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
                androidx.compose.foundation.layout.FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    medications.forEach { med ->
                        val shortLabel = when {
                            med.name.contains("Metformin", ignoreCase = true) -> "Met"
                            med.name.contains("Lisinopril", ignoreCase = true) -> "Lis"
                            med.name.contains("Vitamin", ignoreCase = true) -> "Vit D"
                            med.name.contains("Omega", ignoreCase = true) -> "Omg-3"
                            med.name.length > 6 -> med.name.take(5)
                            else -> med.name
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (med.isTaken) Color(0xFF006A60) else Color(0xFFB0BEC5))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = shortLabel,
                                fontSize = 11.sp,
                                color = Color(0xFF40635C),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun DailyAdherenceCardPreview() {
    com.healthtrack.app.ui.theme.HealthNexaTheme {
        DailyAdherenceCard(
            medications = listOf(
                MedicationItem(name = "Metformin XR", isTaken = true),
                MedicationItem(name = "Lisinopril", isTaken = false),
                MedicationItem(name = "Vitamin D3", isTaken = true)
            )
        )
    }
}

