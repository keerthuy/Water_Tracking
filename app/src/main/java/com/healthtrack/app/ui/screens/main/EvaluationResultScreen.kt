package com.healthtrack.app.ui.screens.main

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.healthtrack.app.ui.model.RiskLevel
import com.healthtrack.app.ui.theme.HealthNexaButton
import com.healthtrack.app.ui.theme.HealthNexaCard
import com.healthtrack.app.ui.theme.HealthNexaOutlinedButton
import com.healthtrack.app.ui.theme.RiskTierBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationResultScreen(
    ingredientName: String,
    amount: Double,
    unit: String,
    condition: String,
    riskLevelStr: String,
    recommendation: String,
    onBackClicked: () -> Unit
) {
    val riskLevel = try {
        RiskLevel.valueOf(riskLevelStr)
    } catch (e: Exception) {
        RiskLevel.MODERATE
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Evaluation Analysis", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Result Header Card
            HealthNexaCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RiskTierBadge(riskLevel = riskLevel)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "$ingredientName ($amount $unit)",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Evaluated against: $condition",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // High Risk Warning Banner Alignment
            if (riskLevel == RiskLevel.HIGH || riskLevel == RiskLevel.CRITICAL) {
                HealthNexaCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.85f),
                    borderColor = MaterialTheme.colorScheme.error
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Warning,
                            contentDescription = "Warning",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .size(28.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "High Clinical Safety Alert",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "The scanned amount ($amount $unit) exceeds the daily recommended threshold for patients with $condition.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            // Clinical Rule Analysis Bar Chart & Metric Breakdown Cards
            HealthNexaCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Clinical Rule Analysis",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                val riskRatio = when (riskLevel) {
                    RiskLevel.LOW -> 0.25f
                    RiskLevel.MODERATE -> 0.55f
                    RiskLevel.HIGH -> 0.85f
                    RiskLevel.CRITICAL -> 1.0f
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Safety Index Ratio",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(riskRatio * 100).toInt()}%",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { riskRatio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = if (riskRatio > 0.7f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Parameter Title & Value Breakdown Table
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ParameterMetricRow(title = "Scanned Ingredient", value = ingredientName)
                    ParameterMetricRow(title = "Serving Amount", value = "$amount $unit")
                    ParameterMetricRow(title = "Target Health Condition", value = condition)
                    ParameterMetricRow(title = "Risk Assessment Tier", value = riskLevel.name)
                }
            }

            // Recommendation Details Card
            HealthNexaCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(28.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Health Recommendation",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = recommendation.ifEmpty {
                                "Ingredient level evaluated against safe medical parameters for $condition."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HealthNexaOutlinedButton(
                    text = "Back",
                    onClick = onBackClicked,
                    modifier = Modifier.weight(1f)
                )
                HealthNexaButton(
                    text = "Evaluate Another",
                    onClick = onBackClicked,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ParameterMetricRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun EvaluationResultScreenPreview() {
    com.healthtrack.app.ui.theme.HealthNexaTheme {
        EvaluationResultScreen(
            ingredientName = "Sodium",
            amount = 1800.0,
            unit = "mg",
            condition = "Hypertension",
            riskLevelStr = "HIGH",
            recommendation = "Sodium intake exceeds 1500mg daily recommendation for Hypertension. Consider reducing portion size.",
            onBackClicked = {}
        )
    }
}

