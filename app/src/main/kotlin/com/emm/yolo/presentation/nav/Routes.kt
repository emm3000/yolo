package com.emm.yolo.presentation.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute : NavKey

@Serializable
data object HistoryRoute : NavKey

@Serializable
data object InsightsRoute : NavKey

@Serializable
data object LogSessionRoute : NavKey

@Serializable
data object RulesRoute : NavKey

@Serializable
data object MainRoute : NavKey

data class NavBarItem(
    val name: String,
    val icon: ImageVector,
)

val TOP_LEVEL_ROUTES: Map<NavKey, NavBarItem> = mapOf(
    HomeRoute to NavBarItem(name = "Home", icon = Icons.Filled.Home),
    HistoryRoute to NavBarItem(name = "History", icon = Icons.Filled.History),
    InsightsRoute to NavBarItem(name = "Insights", icon = Icons.Filled.Insights),
)