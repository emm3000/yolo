package com.emm.yolo.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class TodaySessionUi(
    val minutes: Int,
    val practiceType: String,
    val discomfortLevel: Int
)

data class LastSessionUi(
    val dateLabel: String,
    val minutes: Int,
    val practiceType: String,
    val discomfortLevel: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    todaySession: TodaySessionUi?,
    lastSession: LastSessionUi?,
    streakDays: Int,
    onLogTodayClick: () -> Unit,
    onEditTodayClick: () -> Unit,
    onRecordAudioClick: () -> Unit,
    onAddNotesClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = rememberTodayLabel(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TodayStatusCard(
                todaySession = todaySession,
                onLogTodayClick = onLogTodayClick,
                onEditTodayClick = onEditTodayClick,
                onRecordAudioClick = onRecordAudioClick
            )

            QuickActions(
                onRecordAudioClick = onRecordAudioClick,
                onAddNotesClick = onAddNotesClick
            )

            LastSessionCard(lastSession)

            StreakInfo(streakDays)
        }
    }
}

@Composable
private fun TodayStatusCard(
    todaySession: TodaySessionUi?,
    onLogTodayClick: () -> Unit,
    onEditTodayClick: () -> Unit,
    onRecordAudioClick: () -> Unit
) {
    val colors = if (todaySession == null) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    } else {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    }

    Card(
        colors = colors,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (todaySession == null) {
                Text(
                    text = "No session logged today",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "You haven't practiced yet",
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = onLogTodayClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log today’s session")
                }
            } else {
                Text(
                    text = "Session completed",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${todaySession.minutes} minutes · ${todaySession.practiceType}",
                    style = MaterialTheme.typography.bodyMedium
                )

                AssistChip(
                    onClick = {},
                    label = {
                        Text("Discomfort ${todaySession.discomfortLevel}/5")
                    }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onEditTodayClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Edit session")
                    }

                    TextButton(
                        onClick = onRecordAudioClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add audio")
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActions(
    onRecordAudioClick: () -> Unit,
    onAddNotesClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onRecordAudioClick,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Mic, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Record audio")
        }

        FilledTonalButton(
            onClick = onAddNotesClick,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Edit, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Add notes")
        }
    }
}

@Composable
private fun LastSessionCard(
    lastSession: LastSessionUi?
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Last session",
                style = MaterialTheme.typography.titleSmall
            )

            if (lastSession == null) {
                Text(
                    text = "No previous sessions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(text = lastSession.dateLabel)
                Text(
                    text = "${lastSession.minutes} minutes · ${lastSession.practiceType}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Discomfort ${lastSession.discomfortLevel}/5",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun StreakInfo(streakDays: Int) {
    if (streakDays <= 0) return

    Text(
        text = "$streakDays days in a row",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun rememberTodayLabel(): String {
    val formatter = remember {
        DateTimeFormatter.ofPattern("EEEE, MMM d")
    }
    return LocalDate.now().format(formatter)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            todaySession = TodaySessionUi(
                minutes = 6702,
                practiceType = "vero",
                discomfortLevel = 3047
            ),
            lastSession = LastSessionUi(
                dateLabel = "adipisci",
                minutes = 6717,
                practiceType = "definiebas",
                discomfortLevel = 4285
            ),
            streakDays = 7385,
            onLogTodayClick = {},
            onEditTodayClick = {},
            onRecordAudioClick = {},
            onAddNotesClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview2() {
    MaterialTheme {
        HomeScreen(
            todaySession = null,
            lastSession = LastSessionUi(
                dateLabel = "adipisci",
                minutes = 6717,
                practiceType = "definiebas",
                discomfortLevel = 4285
            ),
            streakDays = 7385,
            onLogTodayClick = {},
            onEditTodayClick = {},
            onRecordAudioClick = {},
            onAddNotesClick = {}
        )
    }
}