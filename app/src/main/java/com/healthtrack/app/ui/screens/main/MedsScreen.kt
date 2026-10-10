package com.healthtrack.app.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Undo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.healthtrack.app.ui.components.DailyAdherenceCard
import com.healthtrack.app.ui.model.MedicationItem
import com.healthtrack.app.ui.theme.HealthNexaButton
import com.healthtrack.app.ui.theme.HealthNexaCard
import com.healthtrack.app.ui.theme.HealthNexaOutlinedButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedsScreen(
    medications: List<MedicationItem>,
    onToggleTaken: (String) -> Unit,
    onDeleteMedication: (String) -> Unit,
    onNavigateToAddMedication: () -> Unit,
    onNavigateToEditMedication: (String) -> Unit = {}
) {


    var selectedHistoryMed by remember { mutableStateOf<MedicationItem?>(null) }
    var medToDelete by remember { mutableStateOf<MedicationItem?>(null) }

    val upcomingMeds = medications.filter { !it.isTaken }
    val loggedMeds = medications.filter { it.isTaken }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddMedication,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add Medication")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Daily Adherence Visualizer Card
            DailyAdherenceCard(medications = medications)

            // Medication Schedule List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Upcoming / Pending Section
                if (upcomingMeds.isNotEmpty()) {
                    item {
                        Text(
                            text = "Upcoming Doses (${upcomingMeds.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    items(upcomingMeds, key = { it.id }) { med ->
                        MedicationCardItem(
                            medication = med,
                            onToggleTaken = { onToggleTaken(med.id) },
                            onEdit = { onNavigateToEditMedication(med.id) },
                            onHistory = { selectedHistoryMed = med },
                            onDelete = { medToDelete = med }
                        )
                    }
                }

                // Logged / Completed Section
                if (loggedMeds.isNotEmpty()) {
                    item {
                        Text(
                            text = "Completed Doses Today (${loggedMeds.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    items(loggedMeds, key = { it.id }) { med ->
                        MedicationCardItem(
                            medication = med,
                            onToggleTaken = { onToggleTaken(med.id) },
                            onEdit = { onNavigateToEditMedication(med.id) },
                            onHistory = { selectedHistoryMed = med },
                            onDelete = { medToDelete = med }
                        )
                    }
                }

                if (medications.isEmpty()) {
                    item {
                        HealthNexaCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Medication,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No medications scheduled",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Tap + or use the button below to add your first medication schedule",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                HealthNexaButton(
                                    text = "+ Add Medication",
                                    onClick = onNavigateToAddMedication
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (medToDelete != null) {
        val med = medToDelete!!
        AlertDialog(
            onDismissRequest = { medToDelete = null },
            title = { Text("Delete Medication") },
            text = { Text("Are you sure you want to delete '${med.name}'?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteMedication(med.id)
                    medToDelete = null
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { medToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Medication History Dialog
    if (selectedHistoryMed != null) {
        val med = selectedHistoryMed!!
        val dateFormatter = remember { SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault()) }

        AlertDialog(
            onDismissRequest = { selectedHistoryMed = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${med.name} History",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Dosage: ${med.dosage} (${med.frequency})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        text = "Adherence History Logs:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    if (med.history.isEmpty()) {
                        Text(
                            text = "No history recorded yet for this medication.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        med.history.forEach { entry ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = entry.action,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = if (entry.action == "Taken") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                        )
                                        if (entry.note.isNotEmpty()) {
                                            Text(
                                                text = entry.note,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                    Text(
                                        text = dateFormatter.format(Date(entry.timestamp)),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedHistoryMed = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
private fun MedicationCardItem(
    medication: MedicationItem,
    onToggleTaken: () -> Unit,
    onEdit: () -> Unit,
    onHistory: () -> Unit,
    onDelete: () -> Unit
) {
    HealthNexaCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (medication.isTaken) {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        } else {
            MaterialTheme.colorScheme.surface
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (medication.isTaken) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Medication,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = medication.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Surface(
                                shape = CircleShape,
                                color = if (medication.isTaken) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = if (medication.isTaken) "Taken" else "Upcoming",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (medication.isTaken) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${medication.dosage} • ${medication.timing}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit medication",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onHistory,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.History,
                            contentDescription = "Medication history",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete medication",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 2.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (medication.isTaken) {
                    HealthNexaOutlinedButton(
                        text = "Undo Taken",
                        icon = Icons.Rounded.Undo,
                        onClick = onToggleTaken,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    HealthNexaButton(
                        text = "Mark Taken",
                        icon = Icons.Rounded.Check,
                        onClick = onToggleTaken,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun MedsScreenPreview() {
    com.healthtrack.app.ui.theme.HealthNexaTheme {
        MedsScreen(
            medications = listOf(
                MedicationItem(name = "Metformin XR", dosage = "500 mg", timing = "8:00 AM", isTaken = true),
                MedicationItem(name = "Lisinopril", dosage = "10 mg", timing = "6:00 PM", isTaken = false)
            ),
            onToggleTaken = {},
            onDeleteMedication = {},
            onNavigateToAddMedication = {},
            onNavigateToEditMedication = {}
        )
    }
}

