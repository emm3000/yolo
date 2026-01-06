package com.emm.yolo.presentation.feature

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emm.yolo.presentation.theme.YoloTheme

@Composable
fun PracticeHistoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1C1E))
            .padding(horizontal = 20.dp)
    ) {
        // 1. Header
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Practice History",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Your recorded English sessions",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        // 2. Resumen rápido (Compacto)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .background(Color(0xFF222427), RoundedCornerShape(8.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryStat("This week", "5")
            SummaryStat("Streak", "12d")
            SummaryStat("Avg time", "15m")
        }

        // 3. Filtros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Speaking", "Listening", "Writing").forEachIndexed { index, label ->
                FilterChipMinimal(label, isSelected = index == 0)
            }
        }

        // 4. Lista de Sesiones (Núcleo)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Ejemplo de item: Hoy
            item { SessionItem("Today", "22:15", "Speaking", "10 min", hasAudio = true) }
            // Ejemplo de item: Ayer
            item { SessionItem("Yesterday", "08:30", "Listening", "20 min", hasText = true) }
            // Ejemplo de item: Fecha fija
            item { SessionItem("Dec 30", "19:00", "Reading", "5 min", hasText = true) }
        }
    }
}

@Composable
fun SessionItem(date: String, time: String, type: String, duration: String, hasAudio: Boolean = false, hasText: Boolean = false) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF222427),
        shape = RoundedCornerShape(8.dp),
        onClick = { /* Open Detail */ }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(date, style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = type,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFA8C7FF),
                        modifier = Modifier
                            .background(Color(0xFF003147), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(duration, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }

            // Indicadores de contenido
            Row {
                if (hasAudio) Icon(Icons.Default.Mic, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                if (hasText) Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun SummaryStat(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FilterChipMinimal(label: String, isSelected: Boolean) {
    Surface(
        color = if (isSelected) Color(0xFFD1E4FF) else Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) null else BorderStroke(1.dp, Color.Gray),
        onClick = {}
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) Color(0xFF003147) else Color.Gray
        )
    }
}

@Preview
@Composable
private fun PracticeHistoryScreenPreview() {
    YoloTheme() {
        PracticeHistoryScreen()
    }
}