package com.emm.yolo.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emm.yolo.presentation.theme.YoloTheme

@Composable
fun LogEnglishSessionScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1C1E))
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Header
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
                    "Jan 4, 2026 • 11:28 PM",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Tipo de Práctica (Chips de alta respuesta)
        Text("Category", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val categories = listOf("🎙 Speaking", "🎧 Listening", "✍️ Writing", "📖 Reading")
            categories.forEach { category ->
                FilterChip(
                    selected = false, // Manejar estado aquí
                    onClick = { },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFF2D3033),
                        labelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Registro Principal (El núcleo)
        Text("Evidence", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF2D3033)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                // Opción A: Audio
                IconButton(
                    onClick = { /* Iniciar grabación */ },
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFF44474A), CircleShape)
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Record", tint = Color(0xFFD1E4FF))
                }
                Text(
                    "Tap to record voice",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color.Gray
                )

                Divider(modifier = Modifier.padding(vertical = 20.dp), color = Color.White.copy(alpha = 0.1f))

                // Opción B: Texto
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("What did you learn? (Optional)", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        cursorColor = Color.White
//                        containerColor = Color.Transparent,
//                        textColor = Color.White,
//                        cursorColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Duración (Mentalidad: 5m es suficiente)
        Text("Duration", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("5m", "10m", "20m", "30m+").forEach { time ->
                OutlinedButton(onClick = { /* Select time */ }) {
                    Text(time, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 6. Acción Final
        Button(
            onClick = { /* Save immediate */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43E188))
        ) {
            Text("Save Session", color = Color(0xFF00391C), fontWeight = FontWeight.Bold)
        }

        // 7. Copy Psicológico
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

@Preview
@Composable
private fun LogEnglishSessionScreenPreview() {
    YoloTheme() {
        LogEnglishSessionScreen(onBack = {})
    }

}