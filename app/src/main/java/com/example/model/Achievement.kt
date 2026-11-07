package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.vector.ImageVector

enum class AchievementCategory(val label: String) {
    MODULES("Modules"),
    QUIZZES("Quizzes"),
    EDITORIAL("Special")
}

data class EditorialAchievement(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: AchievementCategory,
    val icon: ImageVector,
    val isUnlocked: Boolean,
    val currentProgress: Int,
    val targetProgress: Int,
    val tierLabel: String,
    val badgeColorHex: String = "#D63626"
) {
    val progressFraction: Float
        get() = if (targetProgress > 0) {
            (currentProgress.toFloat() / targetProgress).coerceIn(0f, 1f)
        } else if (isUnlocked) 1f else 0f
}

object AchievementCatalog {
    fun buildAchievements(
        stats: ReaderStats,
        modules: List<JournalismModule>,
        quests: List<QuestChallenge>
    ): List<EditorialAchievement> {
        val completedModulesCount = maxOf(
            stats.completedModules,
            modules.count { it.isCompleted || it.progressPct >= 100 }
        )
        val correctQuizzesCount = maxOf(
            stats.correctQuizzes,
            quests.count { it.isCorrect }
        )
        val answeredQuizzesCount = maxOf(
            stats.answeredQuizzes,
            quests.count { it.isAnswered }
        )
        val hasLawMastery = (modules.firstOrNull()?.progressPct ?: 0) >= 100 ||
                (quests.firstOrNull { it.associatedModuleIndex == 0 }?.isCorrect == true)

        val accuracy = if (answeredQuizzesCount > 0) {
            ((correctQuizzesCount.toFloat() / answeredQuizzesCount) * 100).toInt()
        } else 0

        return listOf(
            EditorialAchievement(
                id = "first_byline",
                title = "First Byline",
                subtitle = "Complete your 1st journalism curriculum module",
                category = AchievementCategory.MODULES,
                icon = Icons.Filled.AutoStories,
                isUnlocked = completedModulesCount >= 1,
                currentProgress = completedModulesCount.coerceAtMost(1),
                targetProgress = 1,
                tierLabel = "BRONZE",
                badgeColorHex = "#B87333"
            ),
            EditorialAchievement(
                id = "fact_checker",
                title = "Fact-Checker",
                subtitle = "Answer your first quick check question correctly",
                category = AchievementCategory.QUIZZES,
                icon = Icons.Filled.FactCheck,
                isUnlocked = correctQuizzesCount >= 1,
                currentProgress = correctQuizzesCount.coerceAtMost(1),
                targetProgress = 1,
                tierLabel = "SILVER",
                badgeColorHex = "#7A8A9E"
            ),
            EditorialAchievement(
                id = "press_scholar",
                title = "Press Scholar",
                subtitle = "Master 3 complete modules in the press curriculum",
                category = AchievementCategory.MODULES,
                icon = Icons.Filled.School,
                isUnlocked = completedModulesCount >= 3,
                currentProgress = completedModulesCount.coerceAtMost(3),
                targetProgress = 3,
                tierLabel = "SILVER",
                badgeColorHex = "#4A6E82"
            ),
            EditorialAchievement(
                id = "copy_chief",
                title = "Eagle-Eyed Editor",
                subtitle = "Answer 3 quick check challenges correctly",
                category = AchievementCategory.QUIZZES,
                icon = Icons.Filled.MilitaryTech,
                isUnlocked = correctQuizzesCount >= 3,
                currentProgress = correctQuizzesCount.coerceAtMost(3),
                targetProgress = 3,
                tierLabel = "GOLD",
                badgeColorHex = "#D4AF37"
            ),
            EditorialAchievement(
                id = "press_law_guardian",
                title = "Press Law Guardian",
                subtitle = "Master Campus Journalism Act (R.A. 7079) module & quiz",
                category = AchievementCategory.EDITORIAL,
                icon = Icons.Filled.Gavel,
                isUnlocked = hasLawMastery,
                currentProgress = if (hasLawMastery) 1 else 0,
                targetProgress = 1,
                tierLabel = "SPECIAL",
                badgeColorHex = "#D63626"
            ),
            EditorialAchievement(
                id = "desk_editor",
                title = "Desk Editor",
                subtitle = "Complete 6 modules (half of full curriculum)",
                category = AchievementCategory.MODULES,
                icon = Icons.Filled.WorkspacePremium,
                isUnlocked = completedModulesCount >= 6,
                currentProgress = completedModulesCount.coerceAtMost(6),
                targetProgress = 6,
                tierLabel = "GOLD",
                badgeColorHex = "#C59B27"
            ),
            EditorialAchievement(
                id = "editorial_accuracy",
                title = "Editorial Accuracy",
                subtitle = "Achieve 100% quiz accuracy with 3+ quizzes answered",
                category = AchievementCategory.QUIZZES,
                icon = Icons.Filled.CheckCircle,
                isUnlocked = answeredQuizzesCount >= 3 && accuracy == 100,
                currentProgress = if (answeredQuizzesCount >= 3 && accuracy == 100) 1 else 0,
                targetProgress = 1,
                tierLabel = "PLATINUM",
                badgeColorHex = "#9AA5B1"
            ),
            EditorialAchievement(
                id = "editor_in_chief",
                title = "Editor-in-Chief",
                subtitle = "Complete all 11 core campus journalism modules",
                category = AchievementCategory.MODULES,
                icon = Icons.Filled.EmojiEvents,
                isUnlocked = completedModulesCount >= 11,
                currentProgress = completedModulesCount.coerceAtMost(11),
                targetProgress = 11,
                tierLabel = "LAUREATE",
                badgeColorHex = "#E6C875"
            )
        )
    }
}
