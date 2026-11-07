package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AchievementCategory
import com.example.model.EditorialAchievement
import com.example.ui.theme.DarkLine
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.EditorialCream
import com.example.ui.theme.EditorialCreamExtrude
import com.example.ui.theme.EditorialCrimson
import com.example.ui.theme.EditorialCrimsonDark
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkExtrude
import com.example.ui.theme.GoldFoilSoft
import com.example.ui.theme.GoldLeaf
import com.example.ui.theme.LineBorder

@Composable
fun AchievementsSection(
    achievements: List<EditorialAchievement>,
    modifier: Modifier = Modifier,
    onNavigateQuests: (() -> Unit)? = null
) {
    var selectedCategory by remember { mutableStateOf<AchievementCategory?>(null) }
    var selectedBadgeDetail by remember { mutableStateOf<EditorialAchievement?>(null) }

    val filteredAchievements = remember(selectedCategory, achievements) {
        if (selectedCategory == null) achievements
        else achievements.filter { it.category == selectedCategory }
    }

    val unlockedCount = remember(achievements) { achievements.count { it.isUnlocked } }
    val totalCount = achievements.size
    val overallPct = if (totalCount > 0) ((unlockedCount.toFloat() / totalCount) * 100).toInt() else 0
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section Header
        EditorialSectionHeader(
            title = "Achievements",
            indexLabel = "03",
            actionText = "$unlockedCount/$totalCount UNLOCKED",
            onActionClick = null
        )

        // Summary Progress Bento Card with Liquid Glass sheen
        LiquidGlassBox(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(GoldLeaf, GoldFoilSoft)
                                    )
                                )
                                .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = EditorialInk,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "EDITORIAL MILESTONES",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                ),
                                color = if (isDark) GoldFoilSoft else EditorialCrimson
                            )
                            Text(
                                text = "$unlockedCount of $totalCount Badges Earned",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Progress percentage pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (unlockedCount > 0) EditorialCrimson else LineBorder.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, if (unlockedCount > 0) EditorialCrimsonDark else Color.Transparent)
                    ) {
                        Text(
                            text = "$overallPct%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { (unlockedCount.toFloat() / totalCount.coerceAtLeast(1)).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = GoldLeaf,
                    trackColor = if (isDark) Color(0xFF2A2A32) else Color(0xFFDED6C4)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Filter Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CategoryChip(
                        label = "ALL (${achievements.size})",
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null }
                    )
                    CategoryChip(
                        label = "MODULES (${achievements.count { it.category == AchievementCategory.MODULES }})",
                        selected = selectedCategory == null || selectedCategory == AchievementCategory.MODULES,
                        onClick = {
                            selectedCategory = if (selectedCategory == AchievementCategory.MODULES) null else AchievementCategory.MODULES
                        }
                    )
                    CategoryChip(
                        label = "QUIZZES (${achievements.count { it.category == AchievementCategory.QUIZZES }})",
                        selected = selectedCategory == AchievementCategory.QUIZZES,
                        onClick = {
                            selectedCategory = if (selectedCategory == AchievementCategory.QUIZZES) null else AchievementCategory.QUIZZES
                        }
                    )
                }
            }
        }

        // Horizontal Bento Row of Badges
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp)
        ) {
            items(filteredAchievements, key = { it.id }) { badge ->
                AchievementBadgeCard(
                    achievement = badge,
                    onClick = { selectedBadgeDetail = badge }
                )
            }
        }
    }

    // Badge Detail Dialog
    selectedBadgeDetail?.let { badge ->
        AlertDialog(
            onDismissRequest = { selectedBadgeDetail = null },
            confirmButton = {
                TextButton(onClick = { selectedBadgeDetail = null }) {
                    Text(
                        text = "CLOSE",
                        fontWeight = FontWeight.Bold,
                        color = EditorialCrimson
                    )
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (badge.isUnlocked) {
                                    Brush.linearGradient(listOf(GoldLeaf, GoldFoilSoft))
                                } else {
                                    Brush.linearGradient(listOf(Color(0xFF33333E), Color(0xFF1E1E24)))
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = badge.icon,
                            contentDescription = null,
                            tint = if (badge.isUnlocked) EditorialInk else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = badge.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = if (badge.isUnlocked) "STATUS: UNLOCKED (${badge.tierLabel})" else "STATUS: IN PROGRESS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            ),
                            color = if (badge.isUnlocked) GoldLeaf else EditorialCrimson
                        )
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = badge.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Milestone Criteria",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${badge.currentProgress} / ${badge.targetProgress}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                            color = if (badge.isUnlocked) GoldLeaf else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    LinearProgressIndicator(
                        progress = { badge.progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (badge.isUnlocked) GoldLeaf else EditorialCrimson,
                        trackColor = LineBorder.copy(alpha = 0.2f)
                    )
                }
            },
            shape = RoundedCornerShape(18.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        )
    }
}

@Composable
private fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (selected) EditorialInk else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, if (selected) EditorialCrimson else LineBorder.copy(alpha = 0.3f)),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            ),
            color = if (selected) EditorialCream else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
        )
    }
}

@Composable
private fun AchievementBadgeCard(
    achievement: EditorialAchievement,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    val badgeColor = try {
        Color(android.graphics.Color.parseColor(achievement.badgeColorHex))
    } catch (e: Exception) {
        EditorialCrimson
    }

    Soft3DCard(
        modifier = modifier
            .width(160.dp)
            .height(178.dp)
            .testTag("achievement_badge_${achievement.id}"),
        shape = RoundedCornerShape(16.dp),
        extrudeDepth = 3.5.dp,
        containerColor = if (achievement.isUnlocked) {
            if (isDark) DarkSurface else Color(0xFFFBF8F1)
        } else {
            if (isDark) Color(0xFF19191E) else Color(0xFFF2ECE0)
        },
        extrudeColor = if (achievement.isUnlocked) {
            if (isDark) Color(0xFF0F0F12) else EditorialCreamExtrude
        } else {
            if (isDark) Color(0xFF0C0C0E) else Color(0xFFCDC3B0)
        },
        borderColor = if (achievement.isUnlocked) {
            if (isDark) DarkLine else EditorialInk
        } else {
            LineBorder.copy(alpha = 0.35f)
        },
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Category tag and status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (achievement.isUnlocked) EditorialInk else LineBorder.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = achievement.category.name.take(3),
                        color = if (achievement.isUnlocked) EditorialCream else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                if (achievement.isUnlocked) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = GoldLeaf,
                        border = BorderStroke(1.dp, GoldFoilSoft)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = EditorialInk,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "EARNED",
                                color = EditorialInk,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Black
                                )
                            )
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Locked",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "${achievement.currentProgress}/${achievement.targetProgress}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Center Badge Emblem
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(
                        if (achievement.isUnlocked) {
                            Brush.linearGradient(
                                colors = listOf(
                                    badgeColor,
                                    badgeColor.copy(
                                        red = (badgeColor.red * 0.75f).coerceIn(0f, 1f),
                                        green = (badgeColor.green * 0.75f).coerceIn(0f, 1f),
                                        blue = (badgeColor.blue * 0.75f).coerceIn(0f, 1f)
                                    )
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    if (isDark) Color(0xFF2C2C35) else Color(0xFFDED7C8),
                                    if (isDark) Color(0xFF1E1E24) else Color(0xFFC7BFAe)
                                )
                            )
                        }
                    )
                    .border(
                        1.25.dp,
                        if (achievement.isUnlocked) GoldFoilSoft else LineBorder.copy(alpha = 0.25f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = achievement.icon,
                    contentDescription = null,
                    tint = if (achievement.isUnlocked) Color.White else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp)
                )
            }

            // Bottom Typography and progress bar
            Column {
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = (-0.2).sp
                    ),
                    color = if (achievement.isUnlocked) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = achievement.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        lineHeight = 12.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { achievement.progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = if (achievement.isUnlocked) GoldLeaf else EditorialCrimson,
                    trackColor = if (isDark) Color(0xFF2E2E36) else LineBorder.copy(alpha = 0.2f)
                )
            }
        }
    }
}
