package com.emm.yolo.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emm.yolo.share.theme.BackgroundColor
import com.emm.yolo.share.theme.YoloTheme

@Composable
fun ProgressInsightsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Progress Insights",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Text(
            text = "Analysis of your learning patterns",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InsightCard("Consistency", "78%", modifier = Modifier.weight(1f))
            InsightCard("Best Streak", "15d", modifier = Modifier.weight(1f))
            InsightCard("Total Time", "42h", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Skill Distribution", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        PracticeDistributionChart(
            mapOf("Speaking" to 0.15f, "Listening" to 0.50f, "Writing" to 0.10f, "Reading" to 0.25f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Weekly Activity", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        SimpleActivityChart()

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            color = Color(0xFF2D3033),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("System Observation:", style = MaterialTheme.typography.labelSmall, color = Color(0xFFA8C7FF))
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Speaking sessions are 70% less frequent than Listening. Potential friction detected in oral practice.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Technical Observations", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        OutlinedTextField(
            value = "Focusing on listening during commutes. Need to reduce friction for speaking sessions after 6 PM.",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(vertical = 8.dp),
            textStyle = MaterialTheme.typography.bodySmall,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SimpleActivityChart() {
    Surface(
        color = Color(0xFF222427),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Last 14 Days", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text("Total: 4.2h", style = MaterialTheme.typography.labelSmall, color = Color(0xFFA8C7FF))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                val weeklyData = listOf(
                    0.2f, 0.5f, 0.8f, 0.1f, 0.0f, 0.9f, 0.4f,
                    0.6f, 1.0f, 0.3f, 0.0f, 0.7f, 0.5f, 0.2f
                )

                weeklyData.forEach { heightMultiplier ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(heightMultiplier)
                            .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                            .background(
                                when {
                                    heightMultiplier >= 0.8f -> Color(0xFF43E188)
                                    heightMultiplier > 0.0f -> Color(0xFFA8C7FF)
                                    else -> Color(0xFF370000)
                                }
                            )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("14d ago", style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)
                Text("Today", style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)
            }
        }
    }
}

@Composable
fun InsightCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        color = Color(0xFF222427),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PracticeDistributionChart(data: Map<String, Float>) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        data.forEach { (skill, percentage) ->
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(skill, style = MaterialTheme.typography.bodySmall, color = Color.White)
                    Text("${(percentage * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                LinearProgressIndicator(
                    progress = { percentage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = if (percentage < 0.20f) Color(0xFFBA1A1A) else Color(0xFFA8C7FF),
                    trackColor = Color(0xFF2D3033)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProgressInsightsScreenPreview() {
    YoloTheme {
        ProgressInsightsScreen()
    }
}