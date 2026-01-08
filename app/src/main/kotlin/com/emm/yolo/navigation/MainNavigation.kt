package com.emm.yolo.navigation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.emm.yolo.feature.log.LogEnglishSessionScreen
import com.emm.yolo.feature.log.LogEnglishSessionViewModel
import com.emm.yolo.feature.practice.PracticeRulesScreen
import com.emm.yolo.share.ResultEffect
import com.emm.yolo.share.ResultEventBus
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavigation() {

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

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = {}
                )

                LaunchedEffect(Unit) {
                    launcher.launch(Manifest.permission.RECORD_AUDIO)
                }

                DisposableEffect(Unit) {
                    onDispose {
                        vm.stopRecording()
                    }
                }

                LifecycleStartEffect(Unit) {
                    onStopOrDispose {
                        vm.stopRecording()
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