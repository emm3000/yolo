package com.emm.yolo.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.emm.yolo.feature.ProgressInsightsScreen
import com.emm.yolo.feature.history.PracticeHistoryScreen
import com.emm.yolo.feature.history.PracticeHistoryViewModel
import com.emm.yolo.feature.home.EnglishHome
import com.emm.yolo.feature.home.EnglishHomeViewModel
import com.emm.yolo.share.theme.BackgroundColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainRoot(
    navBackStack: NavBackStack<NavKey>,
    snackbarHostState: SnackbarHostState,
) {

    val navigationState: NavigationState = rememberNavigationState(
        startRoute = HomeRoute,
        topLevelRoutes = TOP_LEVEL_ROUTES.keys,
    )

    val navigator: Navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {
        entry<HomeRoute> {
            val vm: EnglishHomeViewModel = koinViewModel()

            EnglishHome(state = vm.state) { navBackStack.add(LogSessionRoute) }
        }
        entry<HistoryRoute> {
            val vm: PracticeHistoryViewModel = koinViewModel()

            PracticeHistoryScreen(
                state = vm.state,
                onAction = vm::action
            )
        }
        entry<InsightsRoute> {
            ProgressInsightsScreen()
        }
    }

    Scaffold(
        bottomBar = { Csm(navigationState, navigator) },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    containerColor = Color(0xFF2D3033),
                    contentColor = Color.White,
                    snackbarData = snackbarData
                )
            }
        }
    ) { paddingValues ->
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = { navigator.goBack() },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun Csm(navigationState: NavigationState, navigator: Navigator) {

    NavigationBar(
        containerColor = BackgroundColor,
        contentColor = Color.White,
    ) {
        TOP_LEVEL_ROUTES.forEach { (key: NavKey, value: NavBarItem) ->
            val isSelected = key == navigationState.topLevelRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = { navigator.navigate(key) },
                icon = {
                    Icon(
                        imageVector = value.icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = LocalContentColor.current,
                    )
                },
                label = {
                    Text(
                        text = value.name,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = LocalContentColor.current,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}