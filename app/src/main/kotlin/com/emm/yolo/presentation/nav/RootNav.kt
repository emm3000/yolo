package com.emm.yolo.presentation.nav

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.emm.yolo.presentation.feature.EnglishHome
import com.emm.yolo.presentation.feature.PracticeHistoryScreen
import com.emm.yolo.presentation.feature.PracticeRulesScreen
import com.emm.yolo.presentation.feature.ProgressInsightsScreen
import com.emm.yolo.presentation.feature.log.LogEnglishSessionScreen
import com.emm.yolo.presentation.feature.log.LogEnglishSessionViewModel
import com.emm.yolo.presentation.share.ResultEffect
import com.emm.yolo.presentation.share.ResultEventBus
import org.koin.androidx.compose.koinViewModel

@Composable
fun RootNav() {

    val navBackStack = rememberNavBackStack(MainRoute)

    val resultBus = remember { ResultEventBus() }

    NavDisplay(
        backStack = navBackStack,
        onBack = { navBackStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<MainRoute> {

                val hostState = remember { SnackbarHostState() }

                ResultEffect<String>(resultBus) { snackbarMessage ->
                    resultBus.removeResult<String>()
                    hostState.showSnackbar(snackbarMessage)
                }

                MainRoot(
                    navBackStack = navBackStack,
                    snackbarHostState = hostState,
                )
            }
            entry<LogSessionRoute> {

                val vm: LogEnglishSessionViewModel = koinViewModel()

                LaunchedEffect(vm.state.statusMessage) {
                    if (vm.state.statusMessage != null) {
                        resultBus.sendResult(result = vm.state.statusMessage)
                        navBackStack.removeLastOrNull()
                    }
                }

                LogEnglishSessionScreen(
                    state = vm.state,
                    onAction = vm::onAction,
                    onBack = navBackStack::removeLastOrNull,
                )
            }
            entry<RulesRoute> {
                PracticeRulesScreen()
            }
        }
    )
}

@Composable
private fun MainRoot(
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
            EnglishHome {
                navBackStack.add(LogSessionRoute)
            }
        }
        entry<HistoryRoute> {
            PracticeHistoryScreen()
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
        containerColor = Color(0xFF1A1C1E),
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