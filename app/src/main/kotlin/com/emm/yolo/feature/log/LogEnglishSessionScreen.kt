package com.emm.yolo.feature.log

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.outlinedButtonBorder
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emm.yolo.feature.log.recorder.AudioRecord
import com.emm.yolo.share.theme.YoloTheme

@Composable
fun LogEnglishSessionScreen(
    state: LogEnglishSessionUiState = LogEnglishSessionUiState(),
    onAction: (LogEnglishSessionAction) -> Unit = {},
    onBack: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1C1E))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        HeaderLog(state = state, onBack = onBack)

        Spacer(modifier = Modifier.height(32.dp))

        CategoryPickerSection(state = state, onAction = onAction)

        DurationPickerSection(state = state, onAction = onAction)

        Text("Evidence", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF2D3033)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                AnimatedContent(
                    targetState = state.playerState,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.9f)
                        ) togetherWith fadeOut(animationSpec = tween(200))
                    },
                    label = "AudioStateAnimation"
                ) { targetState: PlayerState ->
                    when (targetState) {
                        PlayerState.Idle -> IdleView {
                            onAction(LogEnglishSessionAction.StartRecording)
                        }
                        PlayerState.Recording -> RecordingView {
                            onAction(LogEnglishSessionAction.StopRecording)
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp), color = Color.White.copy(alpha = 0.1f))

                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    state.records.forEach {
                        key(it.name) {
                            PreviewView(
                                duration = it.durationMs.toInt(),
                                isPlaying = state.isPlaying && state.currentRecord == it,
                                playAudio = {
                                    onAction(LogEnglishSessionAction.PlayAudio(it))
                                },
                                deleteAudio = {
                                    onAction(LogEnglishSessionAction.DeleteAudio(it))
                                },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = state.notes,
                    onValueChange = {
                        onAction(LogEnglishSessionAction.SetNotes(it))
                    },
                    placeholder = { Text("What did you learn? (Optional)", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        cursorColor = Color.White,
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onAction(LogEnglishSessionAction.Submit)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43E188))
        ) {
            Text("Save Session", color = Color(0xFF00391C), fontWeight = FontWeight.Bold)
        }

        Text(
            text = "Consistency > Perfection",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
private fun DurationPickerSection(
    state: LogEnglishSessionUiState,
    onAction: (LogEnglishSessionAction) -> Unit
) {
    Text("Duration", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Duration.entries.forEach { time ->
            val isSelected = time == state.duration

            OutlinedButton(
                onClick = {
                    onAction(LogEnglishSessionAction.SetDuration(time))
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) Color(0xFF43E188) else Color(0xFF2D3033),
                    contentColor = Color.White
                ),
                border = if (isSelected) null else outlinedButtonBorder(true),
            ) {
                Text(time.label)
            }
        }
    }
}

@Composable
private fun CategoryPickerSection(
    state: LogEnglishSessionUiState,
    onAction: (LogEnglishSessionAction) -> Unit
) {
    Text("Category", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PracticeType.entries.forEach { category ->
            FilterChip(
                selected = category == state.practiceType,
                onClick = {
                    onAction(LogEnglishSessionAction.SetPracticeType(category))
                },
                label = { Text(category.label) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFF2D3033),
                    labelColor = Color.White,
                )
            )
        }
    }
}

@Composable
private fun HeaderLog(state: LogEnglishSessionUiState, onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "Log Session",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Text(
                text = state.currentDateTime.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        IconButton(onClick = onBack) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }
    }
}

@Composable
fun IdleView(onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFF44474A), CircleShape)
        ) {
            Icon(Icons.Default.Mic, contentDescription = "Record", tint = Color(0xFFD1E4FF))
        }
        Text(
            text = "Tap to add a voice recording",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp),
            color = Color.Gray
        )
    }
}

@Composable
fun RecordingView(onStopClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.4f, // Lo subo a 1.4 para que el efecto sea más claro
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            // Fijamos el tamaño del contenedor para que NADA se mueva fuera
            modifier = Modifier.size(100.dp)
        ) {
            // Círculo de pulso (Fondo)
            Box(
                modifier = Modifier
                    .size(64.dp) // Tamaño fijo
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        // Opcional: que se desvanezca mientras crece
                        alpha = 1f - ((scale - 1f) * 2f).coerceIn(0f, 1f)
                    }
                    .background(Color.Red.copy(alpha = 0.4f), CircleShape)
            )

            // Botón central (No escala para que sea fácil de pulsar)
            IconButton(
                onClick = onStopClick,
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Red, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop",
                    tint = Color.White
                )
            }
        }

        Text(
            "Recording...",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp),
            color = Color.Red
        )
    }
}

@Composable
fun PreviewView(
    duration: Int,
    isPlaying: Boolean,
    playAudio: () -> Unit = {},
    deleteAudio: () -> Unit = {}
) {

    val icon = if (isPlaying.not()) Icons.Default.PlayArrow else Icons.Default.Stop

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color(0xFF3D4146), RoundedCornerShape(32.dp))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = playAudio,
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFD1E4FF), CircleShape)
        ) {
            Icon(icon, contentDescription = "Play", tint = Color.Black)
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text("Recording ready", style = MaterialTheme.typography.labelMedium, color = Color.White)
            Text("${duration}s", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }

        Spacer(Modifier.weight(1f))

        TextButton(onClick = deleteAudio) {
            Text("Delete", color = Color(0xFFFFB4AB))
        }
    }
}

@Preview
@Composable
private fun LogEnglishSessionScreenPreview() {
    YoloTheme {
        LogEnglishSessionScreen(
            state = LogEnglishSessionUiState(
                records = listOf(
                    AudioRecord(
                        path = "accumsan",
                        name = "Ada Torres",
                        durationMs = 1121
                    ),
                    AudioRecord(
                        path = "accumsan 2",
                        name = "Ada Torres 2",
                        durationMs = 1121
                    )
                )
            )
        ) {}
    }
}