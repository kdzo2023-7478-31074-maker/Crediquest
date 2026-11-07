package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AchievementCatalog
import com.example.model.JournalismModule
import com.example.model.QuestChallenge
import com.example.model.ReaderStats
import com.example.ui.components.AchievementsSection
import com.example.ui.components.BentoSteppedChartTile
import com.example.ui.components.BookSpineCard
import com.example.ui.components.EditorialSectionHeader
import com.example.ui.components.LiquidGlassMasthead
import com.example.ui.components.Soft3DButton
import com.example.ui.components.Soft3DCard
import com.example.ui.components.StatCard
import com.example.ui.theme.ClothNavy
import com.example.ui.theme.EditorialCream
import com.example.ui.theme.EditorialCrimson
import com.example.ui.theme.EditorialCrimsonDark
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.GoldLeaf
import com.example.ui.theme.LineBorder
import com.example.viewmodel.NavigationTab

@Composable
fun HomeScreen(
    stats: ReaderStats,
    modules: List<JournalismModule>,
    quests: List<QuestChallenge> = emptyList(),
    onOpenModule: (Int) -> Unit,
    onNavigateTab: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentModule = modules.getOrNull(stats.currentReadingModuleIndex) ?: modules.first()
    val upNextModules = modules.filter { it.id != currentModule.id && it.progressPct < 100 }.take(4)
    val upNextRows = upNextModules.chunked(2)

    val quizzesPct = if (stats.totalQuizzes > 0) {
        ((stats.answeredQuizzes.toFloat() / stats.totalQuizzes) * 100).toInt()
    } else 0

    val achievements = remember(stats, modules, quests) {
        AchievementCatalog.buildAchievements(stats, modules, quests)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 92.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Liquid Glass Editorial Masthead
        item {
            LiquidGlassMasthead(
                progressPercentage = stats.overallReadingPercentage,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("liquid_glass_masthead")
            )
        }

        // 01. Continue Reading Bento Card
        item {
            val bookColor = try {
                Color(android.graphics.Color.parseColor(currentModule.colorHex))
            } catch (e: Exception) {
                ClothNavy
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EditorialSectionHeader(
                    title = if (currentModule.progressPct == 0) "Begin Reading" else "Continue Reading",
                    indexLabel = "01",
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (currentModule.progressPct > 0) EditorialCrimson else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(1.25.dp, EditorialInk)
                ) {
                    Text(
                        text = if (currentModule.progressPct == 100) "COMPLETED" else if (currentModule.progressPct > 0) "IN PROGRESS" else "UNREAD",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.6.sp
                        ),
                        color = if (currentModule.progressPct > 0) Color.White else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Soft3DCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("resume_reading_card"),
                shape = RoundedCornerShape(16.dp),
                extrudeDepth = 4.dp,
                onClick = { onOpenModule(currentModule.id) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Color-blocked Soft 3D Module Index Tile
                    Soft3DCard(
                        modifier = Modifier
                            .width(80.dp)
                            .height(104.dp),
                        containerColor = bookColor,
                        extrudeColor = EditorialInk,
                        borderColor = EditorialInk,
                        shape = RoundedCornerShape(12.dp),
                        extrudeDepth = 3.5.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = EditorialInk
                            ) {
                                Text(
                                    text = "MOD ${currentModule.moduleNumber}",
                                    color = EditorialCream,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 0.5.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }

                            Text(
                                text = currentModule.moduleNumber.toString().padStart(2, '0'),
                                color = EditorialCream,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 26.sp,
                                    letterSpacing = (-0.5).sp
                                )
                            )

                            Text(
                                text = "${currentModule.pageCount}P",
                                color = EditorialCream.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.4.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentModule.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.5.sp,
                                letterSpacing = (-0.2).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "${currentModule.pageCount} Pages · ${currentModule.progressPct}% Read",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (currentModule.progressPct / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (currentModule.progressPct == 100) GoldLeaf else EditorialCrimson,
                            trackColor = LineBorder.copy(alpha = 0.25f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Soft3DButton(
                            onClick = { onOpenModule(currentModule.id) },
                            containerColor = EditorialCrimson,
                            extrudeColor = EditorialCrimsonDark,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("home_read_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when {
                                    currentModule.progressPct == 0 -> "START READING"
                                    currentModule.progressPct == 100 -> "REVIEW MODULE"
                                    else -> "RESUME READING"
                                },
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.6.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // 02. Flush-Aligned Bento Box Reading Record
        item {
            EditorialSectionHeader(
                title = "Reading Record",
                indexLabel = "02"
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BentoSteppedChartTile(
                    modulesPct = stats.overallReadingPercentage,
                    quizzesPct = quizzesPct,
                    accuracyPct = stats.accuracyPercentage,
                    modifier = Modifier
                        .weight(1.45f)
                        .fillMaxHeight()
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        value = "${stats.completedModules}/${stats.totalModules}",
                        label = "modules done",
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    StatCard(
                        value = "${stats.answeredQuizzes}/${stats.totalQuizzes}",
                        label = "quick checks",
                        accentHighlight = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
        }

        // 03. Achievements Section
        item {
            AchievementsSection(
                achievements = achievements,
                onNavigateQuests = { onNavigateTab(NavigationTab.QUESTS) }
            )
        }

        // 04. Up Next in Curriculum — 2-Column Bento Grid
        if (upNextRows.isNotEmpty()) {
            item {
                EditorialSectionHeader(
                    title = "Up Next in Curriculum",
                    indexLabel = "04",
                    actionText = "View All (${modules.size}) ›",
                    onActionClick = { onNavigateTab(NavigationTab.LIBRARY) }
                )
            }

            items(upNextRows) { rowModules ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowModules.forEach { mod ->
                        BookSpineCard(
                            module = mod,
                            onClick = { onOpenModule(mod.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowModules.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
