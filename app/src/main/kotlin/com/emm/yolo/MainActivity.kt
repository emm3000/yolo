package com.emm.yolo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.emm.yolo.navigation.MainNavigation
import com.emm.yolo.share.theme.BackgroundColor
import com.emm.yolo.share.theme.YoloTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(BackgroundColor.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(BackgroundColor.toArgb())
        )
        setContent {
            YoloTheme {
                MainNavigation()
            }
        }
    }
}