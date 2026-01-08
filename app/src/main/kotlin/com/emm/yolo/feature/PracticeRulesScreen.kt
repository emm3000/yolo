package com.emm.yolo.feature

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emm.yolo.share.theme.YoloTheme

@Composable
fun PracticeRulesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1C1E))
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Header
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Practice Rules",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Text(
            text = "Define what counts as real practice",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 2. Session Validity Rules (El "Linter" de tu esfuerzo)
        RuleSectionTitle("Session Validity")
        Surface(
            color = Color(0xFF222427),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Minimum duration",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                Text(
                    "Sessions shorter than this will not count toward your streak.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("2m", "5m", "10m", "Custom").forEach { time ->
                        val isSelected = time == "5m"
                        ConfigChip(time, isSelected)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Practice Types
        RuleSectionTitle("Valid Practice Types")
        Column(modifier = Modifier.fillMaxWidth()) {
            val types = listOf("Speaking", "Listening", "Writing", "Reading")
            types.forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(type, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = true,
                        onCheckedChange = {},
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFA8C7FF))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Streak Logic (Hardcoded rules)
        RuleSectionTitle("Streak Logic")
        Surface(
            color = Color(0xFF222427).copy(alpha = 0.5f),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFF44474A)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                BulletPoint("One valid session per day keeps the streak.")
                BulletPoint("Multiple sessions count as one active day.")
                BulletPoint("Missing one day resets the streak to zero.")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 6. Reminder Settings
        RuleSectionTitle("Notifications")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Daily reminder", color = Color.White)
            Text("21:00", color = Color(0xFFA8C7FF), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 7. Data Integrity (Danger Zone)
        HorizontalDivider(color = Color(0xFF44474A))
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text("Export Practice Data (JSON)", color = Color.White)
        }

        TextButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Reset all data", color = Color(0xFFBA1A1A))
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun RuleSectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun BulletPoint(text: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("•", color = Color(0xFFA8C7FF), modifier = Modifier.padding(end = 8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
fun ConfigChip(label: String, isSelected: Boolean) {
    Surface(
        color = if (isSelected) Color(0xFFD1E4FF) else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFF44474A)),
        onClick = {}
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) Color(0xFF003147) else Color.White
        )
    }
}

@Preview
@Composable
private fun PracticeRulesScreenPreview() {
    YoloTheme() {
        PracticeRulesScreen()
    }
}