package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.model.QuestChallenge
import com.example.ui.components.EditorialSectionHeader
import com.example.ui.components.LiquidGlassBox
import com.example.ui.components.Soft3DCard
import com.example.ui.theme.ClothAmber
import com.example.ui.theme.ClothForest
import com.example.ui.theme.ClothNavy
import com.example.ui.theme.ClothRust
import com.example.ui.theme.EditorialCream
import com.example.ui.theme.EditorialCrimson
import com.example.ui.theme.EditorialCrimsonDark
import com.example.ui.theme.EditorialInk
import com.example.ui.theme.EditorialInkExtrude
import com.example.ui.theme.GoldFoilSoft
import com.example.ui.theme.GoldLeaf

private data class FutureModulePlaceholder(
    val title: String,
    val plannedModuleNumber: Int,
    val targetTerm: String,
    val color: Color,
    val syllabusTopics: List<String>,
    val description: String
)

private val upcomingModules = listOf(
    FutureModulePlaceholder(
        title = "Investigative Reporting & Right to Information",
        plannedModuleNumber = 12,
        targetTerm = "Module Slot A · In Development",
        color = ClothNavy,
        syllabusTopics = listOf("FOI Requests", "Budget Audit Analysis", "Source Protection Protocols"),
        description = "Advanced hands-on investigative methodologies examining university administrative disclosures and student fund accountability."
    ),
    FutureModulePlaceholder(
        title = "Data Journalism & Automated Fact-Checking",
        plannedModuleNumber = 13,
        targetTerm = "Module Slot B · In Development",
        color = ClothForest,
        syllabusTopics = listOf("Spreadsheet Scrubbing", "Data Visualization", "Deepfake & AI Verification"),
        description = "Extracting verifiable insights from campus population demographics, tuition statistics, and digital disinformation trends."
    ),
    FutureModulePlaceholder(
        title = "Mobile Journalism (MoJo) & Multiplatform Video",
        plannedModuleNumber = 14,
        targetTerm = "Module Slot C · In Development",
        color = ClothAmber,
        syllabusTopics = listOf("Vertical Video Storytelling", "On-the-Fly Audio Mixing", "Live Dispatches"),
        description = "Rapid field reporting using native smartphone tools to cover breaking student union rallies and athletics championships."
    ),
    FutureModulePlaceholder(
        title = "Publication Management & Editorial Leadership",
        plannedModuleNumber = 15,
        targetTerm = "Module Slot D · In Development",
        color = ClothRust,
        syllabusTopics = listOf("Staff Succession", "DepEd / CHED Guidelines", "Libel Risk Assessment"),
        description = "Managing editorial boards, managing publication budgets, and safeguarding staff against administrative censorship."
    )
)

@Composable
fun QuestsScreen(
    quests: List<QuestChallenge>,
    onAnswerQuest: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedQuestsCount = quests.count { it.isAnswered }
    val totalXp = quests.filter { it.isCorrect }.sumOf { it.xpReward }

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
                        append("JOURNALISM QUESTS")
                    },
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.6.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Interactive challenges to test your legal, ethical, and writing instincts",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Mastery & Score Soft 3D Bento Banner
        item {
            Soft3DCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quests_score_banner"),
                containerColor = EditorialInk,
                extrudeColor = EditorialInkExtrude,
                borderColor = EditorialInk,
                shape = RoundedCornerShape(16.dp),
                extrudeDepth = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LiquidGlassBox(
                            shape = RoundedCornerShape(12.dp),
                            tintColor = EditorialCrimson,
                            contentPadding = PaddingValues(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.EmojiEvents,
                                contentDescription = null,
                                tint = GoldFoilSoft,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "$totalXp XP EARNED",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    letterSpacing = 0.6.sp
                                ),
                                color = EditorialCream
                            )
                            Text(
                                text = "$completedQuestsCount of ${quests.size} challenges solved",
                                style = MaterialTheme.typography.bodySmall,
                                color = EditorialCream.copy(alpha = 0.78f)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (totalXp >= 150) GoldLeaf else EditorialCrimson,
                        border = BorderStroke(1.dp, EditorialCream.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = if (totalXp >= 150) "SENIOR EDITOR" else "JUNIOR REPORTER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            ),
                            color = if (totalXp >= 150) EditorialInk else Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // Active Quick Check Challenges
        item {
            EditorialSectionHeader(
                title = "Active Quick Check Quests",
                indexLabel = "01"
            )
        }

        items(quests) { quest ->
            Soft3DCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quest_card_${quest.id}"),
                shape = RoundedCornerShape(14.dp),
                extrudeDepth = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.25.dp, EditorialInk),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = quest.category.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 9.5.sp,
                                    letterSpacing = 0.5.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = EditorialCrimson
                        ) {
                            Text(
                                text = "+${quest.xpReward} XP",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp
                                ),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = quest.prompt,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.5.sp,
                            lineHeight = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Options list
                    quest.options.forEachIndexed { index, option ->
                        val isSelected = quest.selectedIndex == index
                        val isCorrectOption = quest.correctIndex == index

                        val backgroundColor = when {
                            quest.isAnswered && isCorrectOption -> EditorialCrimson
                            quest.isAnswered && isSelected && !isCorrectOption -> MaterialTheme.colorScheme.errorContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                        }

                        val textColor = when {
                            quest.isAnswered && isCorrectOption -> Color.White
                            quest.isAnswered && isSelected && !isCorrectOption -> MaterialTheme.colorScheme.onErrorContainer
                            else -> MaterialTheme.colorScheme.onSurface
                        }

                        val borderColor = when {
                            quest.isAnswered && isCorrectOption -> EditorialInk
                            quest.isAnswered && isSelected && !isCorrectOption -> MaterialTheme.colorScheme.error
                            else -> EditorialInk.copy(alpha = 0.35f)
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable(enabled = !quest.isAnswered) {
                                    onAnswerQuest(quest.id, index)
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = backgroundColor,
                            border = BorderStroke(1.25.dp, borderColor)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${('A' + index)}. ",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black),
                                    color = textColor
                                )
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = textColor,
                                    modifier = Modifier.weight(1f)
                                )
                                if (quest.isAnswered && isCorrectOption) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else if (quest.isAnswered && isSelected && !isCorrectOption) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Incorrect",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Liquid Glass Explanation Feedback
                    AnimatedVisibility(visible = quest.isAnswered) {
                        LiquidGlassBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = if (quest.isCorrect) "✓ CORRECT ANSWER" else "⚠ REVIEW NOTE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 0.6.sp
                                    ),
                                    color = if (quest.isCorrect) EditorialCrimson else MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = quest.explanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Future Modules Placeholders
        item {
            Spacer(modifier = Modifier.height(6.dp))
            EditorialSectionHeader(
                title = "Upcoming Modules",
                indexLabel = "02"
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Prepared placeholders for subsequent syllabus module integration",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(upcomingModules) { futureMod ->
            Soft3DCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("placeholder_module_${futureMod.plannedModuleNumber}"),
                shape = RoundedCornerShape(14.dp),
                extrudeDepth = 3.5.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = futureMod.color.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, futureMod.color.copy(alpha = 0.45f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.HourglassEmpty,
                                    contentDescription = null,
                                    tint = futureMod.color,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = futureMod.targetTerm,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = futureMod.color
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = EditorialInk
                        ) {
                            Text(
                                text = "MODULE ${futureMod.plannedModuleNumber}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Black
                                ),
                                color = EditorialCream,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = futureMod.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = futureMod.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        futureMod.syllabusTopics.forEach { topic ->
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(0.75.dp, EditorialInk.copy(alpha = 0.25f))
                            ) {
                                Text(
                                    text = topic,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
