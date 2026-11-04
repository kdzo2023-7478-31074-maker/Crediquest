package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.BookmarkItem
import com.example.model.JournalismModule
import com.example.model.ModuleCatalog
import com.example.model.QuestChallenge
import com.example.model.ReaderStats
import com.example.model.StudentProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject

enum class NavigationTab(val title: String) {
    HOME("Home"),
    LIBRARY("Library"),
    QUESTS("Quests"),
    PROFILE("Preferences")
}

data class AppUiState(
    val currentTab: NavigationTab = NavigationTab.HOME,
    val isReaderOpen: Boolean = false,
    val activeModuleIndex: Int = 0,
    val activePageIndex: Int = 0,
    val modules: List<JournalismModule> = ModuleCatalog.modules,
    val stats: ReaderStats = ReaderStats(),
    val bookmarks: List<BookmarkItem> = emptyList(),
    val quests: List<QuestChallenge> = ModuleCatalog.sampleQuests,
    val profile: StudentProfile = StudentProfile()
)

class ReaderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun selectTab(tab: NavigationTab) {
        _uiState.update { it.copy(currentTab = tab, isReaderOpen = false) }
    }

    fun openReader(moduleIndex: Int, pageIndex: Int = 0) {
        val safeModule = moduleIndex.coerceIn(0, _uiState.value.modules.lastIndex)
        _uiState.update { current ->
            current.copy(
                isReaderOpen = true,
                activeModuleIndex = safeModule,
                activePageIndex = pageIndex,
                stats = current.stats.copy(
                    currentReadingModuleIndex = safeModule,
                    currentReadingPageIndex = pageIndex
                )
            )
        }
    }

    fun closeReader() {
        _uiState.update { it.copy(isReaderOpen = false) }
    }

    fun answerQuest(questId: String, selectedIndex: Int) {
        _uiState.update { current ->
            val updatedQuests = current.quests.map { quest ->
                if (quest.id == questId) {
                    quest.copy(selectedIndex = selectedIndex)
                } else quest
            }
            val answeredCount = updatedQuests.count { it.isAnswered }
            val correctCount = updatedQuests.count { it.isCorrect }

            current.copy(
                quests = updatedQuests,
                stats = current.stats.copy(
                    answeredQuizzes = answeredCount,
                    correctQuizzes = correctCount
                )
            )
        }
    }

    fun updateTheme(themeName: String) {
        _uiState.update { current ->
            current.copy(
                stats = current.stats.copy(theme = themeName),
                profile = current.profile.copy(readerTheme = themeName)
            )
        }
    }

    fun updateFontSize(delta: Int) {
        _uiState.update { current ->
            val newFs = (current.stats.fontSize + delta * 2).coerceIn(13, 26)
            current.copy(stats = current.stats.copy(fontSize = newFs))
        }
    }

    fun toggleAutoBookmark() {
        _uiState.update { current ->
            current.copy(profile = current.profile.copy(autoBookmark = !current.profile.autoBookmark))
        }
    }

    fun toggleHapticFeedback() {
        _uiState.update { current ->
            current.copy(profile = current.profile.copy(hapticFeedback = !current.profile.hapticFeedback))
        }
    }

    fun handleBridgeMessage(messageJson: String) {
        try {
            val json = JSONObject(messageJson)
            val action = json.optString("action")
            val payload = json.optJSONObject("payload") ?: return

            when (action) {
                "stats_updated" -> {
                    val completed = payload.optInt("completedModules", 0)
                    val totalM = payload.optInt("totalModules", 11)
                    val totalQ = payload.optInt("totalQuizzes", 4)
                    val answeredQ = payload.optInt("answeredQuizzes", 0)
                    val correctQ = payload.optInt("correctQuizzes", 0)
                    val curM = payload.optInt("currentModuleIndex", 0)
                    val curP = payload.optInt("currentPageIndex", 0)
                    val theme = payload.optString("theme", "paper")
                    val fs = payload.optInt("fontSize", 17)
                    val bmArray = payload.optJSONArray("bookmarks")

                    val bookmarkList = mutableListOf<BookmarkItem>()
                    if (bmArray != null) {
                        for (i in 0 until bmArray.length()) {
                            val bObj = bmArray.getJSONObject(i)
                            bookmarkList.add(
                                BookmarkItem(
                                    moduleIndex = bObj.optInt("moduleIndex"),
                                    pageIndex = bObj.optInt("pageIndex"),
                                    moduleTitle = bObj.optString("moduleTitle"),
                                    pageTitle = bObj.optString("pageTitle")
                                )
                            )
                        }
                    }

                    _uiState.update { current ->
                        current.copy(
                            stats = current.stats.copy(
                                completedModules = completed,
                                totalModules = totalM,
                                totalQuizzes = totalQ,
                                answeredQuizzes = answeredQ,
                                correctQuizzes = correctQ,
                                currentReadingModuleIndex = curM,
                                currentReadingPageIndex = curP,
                                theme = theme,
                                fontSize = fs,
                                bookmarksCount = bookmarkList.size
                            ),
                            bookmarks = bookmarkList
                        )
                    }
                }
                "reader_opened" -> {
                    val m = payload.optInt("moduleIndex", 0)
                    val p = payload.optInt("pageIndex", 0)
                    _uiState.update { current ->
                        current.copy(
                            isReaderOpen = true,
                            activeModuleIndex = m,
                            activePageIndex = p
                        )
                    }
                }
                "reader_closed" -> {
                    _uiState.update { it.copy(isReaderOpen = false) }
                }
                "bookmark_toggled" -> {
                    val m = payload.optInt("moduleIndex", 0)
                    val p = payload.optInt("pageIndex", 0)
                    val isBookmarked = payload.optBoolean("isBookmarked", false)
                    val title = payload.optString("moduleTitle", "")

                    _uiState.update { current ->
                        val updatedBookmarks = current.bookmarks.toMutableList()
                        if (isBookmarked) {
                            if (updatedBookmarks.none { it.moduleIndex == m && it.pageIndex == p }) {
                                updatedBookmarks.add(
                                    BookmarkItem(m, p, title, "Page ${p + 1}")
                                )
                            }
                        } else {
                            updatedBookmarks.removeAll { it.moduleIndex == m && it.pageIndex == p }
                        }
                        current.copy(bookmarks = updatedBookmarks)
                    }
                }
                "quiz_answered" -> {
                    val m = payload.optInt("moduleIndex", 0)
                    val p = payload.optInt("pageIndex", 0)
                    val isCorrect = payload.optBoolean("isCorrect", false)
                    val choiceIndex = payload.optInt("choiceIndex", 0)

                    _uiState.update { current ->
                        val updatedQuests = current.quests.map { quest ->
                            if (quest.associatedModuleIndex == m) {
                                quest.copy(selectedIndex = choiceIndex)
                            } else quest
                        }
                        current.copy(quests = updatedQuests)
                    }
                }
                "theme_changed" -> {
                    val theme = payload.optString("theme", "paper")
                    updateTheme(theme)
                }
                "font_size_changed" -> {
                    val delta = payload.optInt("delta", 0)
                    updateFontSize(delta)
                }
                else -> {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
