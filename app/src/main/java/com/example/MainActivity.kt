package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CrediQuestBottomBar
import com.example.ui.components.LiquidGlassBox
import com.example.ui.components.Soft3DCard
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuestsScreen
import com.example.ui.screens.ReaderScreen
import com.example.ui.theme.EditorialCream
import com.example.ui.theme.EditorialCrimson
import com.example.ui.theme.EditorialCrimsonDark
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.GoldFoilSoft
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.NavigationTab
import com.example.viewmodel.ReaderViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ReaderViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            var showSplash by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                delay(900L)
                showSplash = false
            }

            val isDarkTheme = uiState.stats.theme == "dark"

            MyApplicationTheme(darkTheme = isDarkTheme) {
                Crossfade(
                    targetState = showSplash,
                    animationSpec = tween(durationMillis = 320),
                    label = "splash_crossfade"
                ) { isSplash ->
                    if (isSplash) {
                        CrediQuestLoadingScreen()
                    } else if (uiState.isReaderOpen) {
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
                                                quests = uiState.quests,
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
}

@Composable
private fun CrediQuestLoadingScreen() {
    var startProgress by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        startProgress = true
    }
    val progress by animateFloatAsState(
        targetValue = if (startProgress) 1f else 0.12f,
        animationSpec = tween(durationMillis = 820, easing = FastOutSlowInEasing),
        label = "splash_progress"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EditorialInk)
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            EditorialCrimson.copy(alpha = 0.22f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.5f, size.height * 0.42f),
                        radius = size.maxDimension * 0.55f
                    )
                )
            }
            .padding(horizontal = 28.dp)
            .testTag("crediquest_loading_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Soft 3D Bento Emblem Card
            Soft3DCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = EditorialCream,
                extrudeColor = EditorialCrimsonDark,
                borderColor = EditorialCrimson,
                shape = RoundedCornerShape(22.dp),
                extrudeDepth = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 28.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // App Emblem
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(EditorialInk)
                            .border(2.dp, EditorialCrimson, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.crediquest_app_logo_1793973998516),
                            contentDescription = "CrediQuest Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = EditorialCrimson, fontWeight = FontWeight.Black)) {
                                append(".")
                            }
                            append("CrediQuest")
                        },
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp,
                            letterSpacing = 0.8.sp
                        ),
                        color = EditorialInk
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = EditorialInk
                    ) {
                        Text(
                            text = "CAMPUS PRESS · R.A. 7079",
                            color = GoldFoilSoft,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Liquid Glass Loading Bar Pill
            LiquidGlassBox(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                tintColor = Color(0xFF222226),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LOADING CURRICULUM",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp
                            ),
                            color = EditorialCream.copy(alpha = 0.85f)
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            ),
                            color = EditorialCrimson
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = EditorialCrimson,
                        trackColor = Color.White.copy(alpha = 0.14f)
                    )
                }
            }
        }
    }
}
