package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ReaderStats
import com.example.ui.components.BentoSteppedChartTile
import com.example.ui.components.EditorialSectionHeader
import com.example.ui.components.LiquidGlassBox
import com.example.ui.components.Soft3DButton
import com.example.ui.components.Soft3DCard
import com.example.ui.components.StatCard
import com.example.ui.theme.DarkInk
import com.example.ui.theme.DarkLine
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EditorialCream
import com.example.ui.theme.EditorialCrimson
import com.example.ui.theme.EditorialCrimsonDark
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkExtrude
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperLight
import com.example.ui.theme.SepiaInk
import com.example.ui.theme.SepiaLine
import com.example.ui.theme.SepiaPaper

@Composable
fun ProfileScreen(
    stats: ReaderStats,
    autoBookmark: Boolean,
    hapticFeedback: Boolean,
    onSelectTheme: (String) -> Unit,
    onChangeFontSize: (Int) -> Unit,
    onToggleAutoBookmark: () -> Unit,
    onToggleHaptic: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quizzesPct = if (stats.totalQuizzes > 0) {
        ((stats.answeredQuizzes.toFloat() / stats.totalQuizzes) * 100).toInt()
    } else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Column {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = EditorialCrimson, fontWeight = FontWeight.Black)) {
                            append(".")
                        }
                        append("STATS & PREFERENCES")
                    },
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.6.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Curriculum progress metrics and reader appearance settings",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 01. Academic Reading Statistics Bento Section
        item {
            EditorialSectionHeader(
                title = "Academic Reading Statistics",
                indexLabel = "01"
            )
            Spacer(modifier = Modifier.height(10.dp))

            Soft3DCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stats_summary_card"),
                shape = RoundedCornerShape(16.dp),
                extrudeDepth = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "COURSE CURRICULUM COMPLETION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.6.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            shape = RoundedCornerShape(5.dp),
                            color = EditorialCrimson
                        ) {
                            Text(
                                text = "${stats.overallReadingPercentage}%",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { (stats.overallReadingPercentage / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = EditorialCrimson,
                        trackColor = LineBorder.copy(alpha = 0.25f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCard(
                            value = "${stats.completedModules}/${stats.totalModules}",
                            label = "modules",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            value = "${stats.answeredQuizzes}",
                            label = "quizzes",
                            accentHighlight = true,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            value = "${stats.accuracyPercentage}%",
                            label = "accuracy",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            value = "${stats.bookmarksCount}",
                            label = "saved",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            BentoSteppedChartTile(
                modulesPct = stats.overallReadingPercentage,
                quizzesPct = quizzesPct,
                accuracyPct = stats.accuracyPercentage
            )
        }

        // 02. Reader Preferences Section
        item {
            EditorialSectionHeader(
                title = "Reader Preferences",
                indexLabel = "02"
            )
            Spacer(modifier = Modifier.height(10.dp))

            Soft3DCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reader_preferences_card"),
                shape = RoundedCornerShape(16.dp),
                extrudeDepth = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Theme Selector
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Palette,
                            contentDescription = null,
                            tint = EditorialCrimson,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "READING BACKGROUND THEME",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.6.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Paper
                        val isPaper = stats.theme == "paper"
                        Soft3DCard(
                            modifier = Modifier.weight(1f),
                            containerColor = PaperLight,
                            extrudeColor = if (isPaper) EditorialCrimson else EditorialInk,
                            borderColor = if (isPaper) EditorialCrimson else EditorialInk,
                            shape = RoundedCornerShape(10.dp),
                            extrudeDepth = if (isPaper) 4.dp else 2.5.dp,
                            onClick = { onSelectTheme("paper") }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "PAPER",
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF141414),
                                    fontSize = 12.sp,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Default",
                                    fontSize = 10.sp,
                                    color = Color(0xFF5C5852)
                                )
                            }
                        }

                        // Sepia
                        val isSepia = stats.theme == "sepia"
                        Soft3DCard(
                            modifier = Modifier.weight(1f),
                            containerColor = SepiaPaper,
                            extrudeColor = if (isSepia) EditorialCrimson else EditorialInk,
                            borderColor = if (isSepia) EditorialCrimson else SepiaLine,
                            shape = RoundedCornerShape(10.dp),
                            extrudeDepth = if (isSepia) 4.dp else 2.5.dp,
                            onClick = { onSelectTheme("sepia") }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "SEPIA",
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF3B2F1E),
                                    fontSize = 12.sp,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Warm",
                                    fontSize = 10.sp,
                                    color = Color(0xFF7A6A50)
                                )
                            }
                        }

                        // Dark
                        val isDark = stats.theme == "dark"
                        Soft3DCard(
                            modifier = Modifier.weight(1f),
                            containerColor = DarkSurface,
                            extrudeColor = if (isDark) EditorialCrimson else Color.Black,
                            borderColor = if (isDark) EditorialCrimson else DarkLine,
                            shape = RoundedCornerShape(10.dp),
                            extrudeDepth = if (isDark) 4.dp else 2.5.dp,
                            onClick = { onSelectTheme("dark") }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "DARK",
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFEDEAE1),
                                    fontSize = 12.sp,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Night",
                                    fontSize = 10.sp,
                                    color = Color(0xFF8D8A82)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Font Size Controller with Soft 3D Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.FormatSize,
                                contentDescription = null,
                                tint = EditorialCrimson,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Text Scale (${stats.fontSize}px)",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Adjust reading text size",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Soft3DButton(
                                onClick = { onChangeFontSize(-1) },
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                extrudeColor = EditorialInk,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                modifier = Modifier.testTag("btn_font_decrease")
                            ) {
                                Text(
                                    text = "A−",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Soft3DButton(
                                onClick = { onChangeFontSize(1) },
                                containerColor = EditorialCrimson,
                                extrudeColor = EditorialCrimsonDark,
                                contentColor = Color.White,
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                modifier = Modifier.testTag("btn_font_increase")
                            ) {
                                Text(
                                    text = "A+",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black),
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Liquid Glass Live Typography Preview Card
                    LiquidGlassBox(
                        shape = RoundedCornerShape(12.dp),
                        tintColor = when (stats.theme) {
                            "sepia" -> SepiaPaper
                            "dark" -> DarkSurface
                            else -> PaperLight
                        },
                        contentPadding = PaddingValues(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Column {
                            Text(
                                text = "LIVE TYPOGRAPHY PREVIEW",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.8.sp
                                ),
                                color = EditorialCrimson
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "“The State shall promote the development and growth of campus journalism as a means of strengthening ethical values.”",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontSize = stats.fontSize.sp,
                                    lineHeight = (stats.fontSize * 1.5).sp
                                ),
                                color = when (stats.theme) {
                                    "sepia" -> SepiaInk
                                    "dark" -> DarkInk
                                    else -> InkPrimary
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Auto Bookmark Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Bookmark,
                                contentDescription = null,
                                tint = EditorialCrimson,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Auto-save Reading Position",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Automatically resume last visited page",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = autoBookmark,
                            onCheckedChange = { onToggleAutoBookmark() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = EditorialCrimson
                            ),
                            modifier = Modifier.testTag("switch_auto_bookmark")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Haptic Feedback Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Vibration,
                                contentDescription = null,
                                tint = EditorialCrimson,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Tactile Haptics",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Subtle vibration on page turn and quiz submit",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = hapticFeedback,
                            onCheckedChange = { onToggleHaptic() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = EditorialCrimson
                            ),
                            modifier = Modifier.testTag("switch_haptic_feedback")
                        )
                    }
                }
            }
        }

        // 03. Legal & Course Attribution Bento Card
        item {
            EditorialSectionHeader(
                title = "Legal Framework & Attribution",
                indexLabel = "03"
            )
            Spacer(modifier = Modifier.height(10.dp))

            Soft3DCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("about_app_card"),
                containerColor = EditorialInk,
                extrudeColor = EditorialInkExtrude,
                borderColor = EditorialInk,
                shape = RoundedCornerShape(16.dp),
                extrudeDepth = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = EditorialCrimson,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "REPUBLIC ACT NO. 7079",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.6.sp
                            ),
                            color = EditorialCream
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "CrediQuest is developed under the auspices of Republic Act No. 7079 (Campus Journalism Act of 1991), enacted to promote student journalism, editorial ethics, and publication stewardship across Philippine academic institutions.",
                        style = MaterialTheme.typography.bodySmall,
                        color = EditorialCream.copy(alpha = 0.85f),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Course: Eng Ed 23 · Bicol University Polangui\nVersion 1.0 (Hybrid Native Android Container)",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = EditorialCream.copy(alpha = 0.65f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
