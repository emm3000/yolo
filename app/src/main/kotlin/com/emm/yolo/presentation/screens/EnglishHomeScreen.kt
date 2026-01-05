package com.emm.yolo.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emm.yolo.presentation.theme.YoloTheme

@Composable
fun EnglishHome() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1C1E))
            .padding(24.dp)
    ) {
        // 1. Header
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(
                text = "Daily English",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Sunday, Jan 4, 2026",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Estado del día (Presión psicológica)
        val hasPracticed = false // Estado dinámico

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (hasPracticed) Color(0xFF1B3D2F) else Color(0xFF370000))
                .border(
                    width = 1.dp,
                    color = if (hasPracticed) Color(0xFF43E188) else Color(0xFFBA1A1A),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Did you practice today?",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (hasPracticed) "PRACTICED" else "NOT PRACTICED",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (hasPracticed) Color(0xFF43E188) else Color(0xFFBA1A1A)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 3. Progreso acumulado (Métricas crudas)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MetricItem("Streak", "12", "days")
            MetricItem("Total", "148", "sessions")
            MetricItem("Month", "22", "active")
        }

        Spacer(modifier = Modifier.weight(1f))

        // 5. Refuerzo cognitivo
        Text(
            text = "Consistency beats intensity.",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.5f),
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Acción Principal
        Button(
            onClick = { /* Log session */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD1E4FF))
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = null,
                tint = Color(0xFF003147)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Log English Session",
                color = Color(0xFF003147),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MetricItem(label: String, value: String, unit: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(unit, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Preview
@Composable
private fun EnglishHomePreview() {
    YoloTheme() {
        EnglishHome()
    }
}