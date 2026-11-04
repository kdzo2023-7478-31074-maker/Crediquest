package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CrediQuestBottomBar
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuestsScreen
import com.example.ui.screens.ReaderScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.NavigationTab
import com.example.viewmodel.ReaderViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ReaderViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val isDarkTheme = uiState.stats.theme == "dark"

            MyApplicationTheme(darkTheme = isDarkTheme) {
                if (uiState.isReaderOpen) {
                    val isBookmarked = uiState.bookmarks.any {
                        it.moduleIndex == uiState.activeModuleIndex && it.pageIndex == uiState.activePageIndex
                    }
                    ReaderScreen(
                        moduleIndex = uiState.activeModuleIndex,
                        pageIndex = uiState.activePageIndex,
                        modules = uiState.modules,
                        isCurrentBookmarked = isBookmarked,
                        currentTheme = uiState.stats.theme,
                        fontSize = uiState.stats.fontSize,
                        onBridgeMessage = { msg -> viewModel.handleBridgeMessage(msg) },
                        onCloseReader = { viewModel.closeReader() }
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = MaterialTheme.colorScheme.background,
                        bottomBar = {
                            CrediQuestBottomBar(
                                currentTab = uiState.currentTab,
                                onTabSelected = { tab -> viewModel.selectTab(tab) }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                                .windowInsetsPadding(WindowInsets.statusBars)
                                .padding(innerPadding)
                        ) {
                            AnimatedContent(
                                targetState = uiState.currentTab,
                                transitionSpec = { fadeIn() togetherWith fadeOut() },
                                label = "tab_transition"
                            ) { tab ->
                                when (tab) {
                                    NavigationTab.HOME -> {
                                        HomeScreen(
                                            stats = uiState.stats,
                                            modules = uiState.modules,
                                            onOpenModule = { modId ->
                                                viewModel.openReader(modId, 0)
                                            },
                                            onNavigateTab = { targetTab ->
                                                viewModel.selectTab(targetTab)
                                            }
                                        )
                                    }
                                    NavigationTab.LIBRARY -> {
                                        LibraryScreen(
                                            modules = uiState.modules,
                                            bookmarks = uiState.bookmarks,
                                            onOpenModule = { modId, page ->
                                                viewModel.openReader(modId, page)
                                            }
                                        )
                                    }
                                    NavigationTab.QUESTS -> {
                                        QuestsScreen(
                                            quests = uiState.quests,
                                            onAnswerQuest = { qId, choice ->
                                                viewModel.answerQuest(qId, choice)
                                            }
                                        )
                                    }
                                    NavigationTab.PROFILE -> {
                                        ProfileScreen(
                                            stats = uiState.stats,
                                            autoBookmark = uiState.profile.autoBookmark,
                                            hapticFeedback = uiState.profile.hapticFeedback,
                                            onSelectTheme = { theme ->
                                                viewModel.updateTheme(theme)
                                            },
                                            onChangeFontSize = { delta ->
                                                viewModel.updateFontSize(delta)
                                            },
                                            onToggleAutoBookmark = {
                                                viewModel.toggleAutoBookmark()
                                            },
                                            onToggleHaptic = {
                                                viewModel.toggleHapticFeedback()
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
