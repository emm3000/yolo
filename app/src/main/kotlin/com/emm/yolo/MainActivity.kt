package com.emm.yolo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.emm.yolo.navigation.RootNav
import com.emm.yolo.share.theme.YoloTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color(0xFF1A1C1E).toArgb()),
            navigationBarStyle = SystemBarStyle.dark(Color(0xFF1A1C1E).toArgb())
        )
        setContent {
            YoloTheme {
                RootNav()
            }
        }
    }
}