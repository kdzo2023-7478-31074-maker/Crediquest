package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.model.BookmarkItem
import com.example.model.JournalismModule
import com.example.model.matchesQuery
import com.example.ui.components.BookSpineCard
import com.example.ui.components.CrediQuestSearchBar
import com.example.ui.components.EditorialSectionHeader
import com.example.ui.components.LiquidGlassBox
import com.example.ui.components.Soft3DCard
import com.example.ui.theme.EditorialCrimson
import com.example.ui.theme.EditorialInk

enum class LibraryFilter(val label: String) {
    ALL("All Modules"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    BOOKMARKS("Bookmarks")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LibraryScreen(
    modules: List<JournalismModule>,
    bookmarks: List<BookmarkItem>,
    onOpenModule: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(LibraryFilter.ALL) }

    val searchResults = remember(searchQuery, modules) {
        if (searchQuery.isNotBlank()) {
            modules.mapNotNull { it.matchesQuery(searchQuery) }
        } else {
            null
        }
    }

    val baseModules = if (searchResults != null) {
        searchResults.map { it.module }
    } else {
        modules
    }

    val filteredModules = when (selectedFilter) {
        LibraryFilter.ALL -> baseModules
        LibraryFilter.IN_PROGRESS -> baseModules.filter { it.progressPct in 1..99 }
        LibraryFilter.COMPLETED -> baseModules.filter { it.progressPct == 100 }
        LibraryFilter.BOOKMARKS -> baseModules
    }

    val filteredBookmarks = remember(searchQuery, bookmarks) {
        if (searchQuery.isBlank()) {
            bookmarks
        } else {
            bookmarks.filter {
                it.moduleTitle.contains(searchQuery, ignoreCase = true) ||
                it.pageTitle.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    val suggestedSearches = listOf("R.A. 7079", "Headline", "Inverted pyramid", "Copyreading", "5Ws", "Photojournalism")

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Library Header & Liquid Glass Search Bar
        item(span = { GridItemSpan(2) }) {
            Column {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = EditorialCrimson, fontWeight = FontWeight.Black)) {
                            append(".")
                        }
                        append("LIBRARY SHELF")
                    },
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.6.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "All 11 modules covering the campus press curriculum",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))

                CrediQuestSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onClear = { searchQuery = "" },
                    placeholderText = "Search title, keywords, or content...",
                    testTag = "library_search_bar"
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(LibraryFilter.values()) { filter ->
                        val count = when (filter) {
                            LibraryFilter.ALL -> baseModules.size
                            LibraryFilter.IN_PROGRESS -> baseModules.count { it.progressPct in 1..99 }
                            LibraryFilter.COMPLETED -> baseModules.count { it.progressPct == 100 }
                            LibraryFilter.BOOKMARKS -> filteredBookmarks.size
                        }
                        val isSelected = selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filter },
                            label = {
                                Text(
                                    text = "${filter.label} ($count)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold
                                    )
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = EditorialInk.copy(alpha = 0.35f),
                                selectedBorderColor = EditorialInk,
                                borderWidth = 1.25.dp,
                                selectedBorderWidth = 1.5.dp
                            ),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EditorialCrimson,
                                selectedLabelColor = Color.White,
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier.testTag("filter_chip_${filter.name.lowercase()}")
                        )
                    }
                }
            }
        }

        // Active Search Query Notification Pill
        if (searchQuery.isNotBlank()) {
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Results matching \"$searchQuery\" (${filteredModules.size})",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "RESET",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                        color = EditorialCrimson,
                        modifier = Modifier
                            .clickable { searchQuery = "" }
                            .padding(4.dp)
                    )
                }
            }
        }

        // Bookmarks Bento Section
        if (selectedFilter == LibraryFilter.BOOKMARKS || (selectedFilter == LibraryFilter.ALL && filteredBookmarks.isNotEmpty() && searchQuery.isBlank())) {
            item(span = { GridItemSpan(2) }) {
                Column {
                    EditorialSectionHeader(
                        title = "Saved Bookmarks (${filteredBookmarks.size})",
                        indexLabel = "01"
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (filteredBookmarks.isEmpty()) {
                        LiquidGlassBox(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text(
                                text = if (searchQuery.isNotBlank()) "No bookmarks matched \"$searchQuery\"." else "No bookmarks saved yet. Tap the bookmark icon while reading any module to save pages here.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            filteredBookmarks.forEach { bm ->
                                Soft3DCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    extrudeDepth = 3.dp,
                                    onClick = { onOpenModule(bm.moduleIndex, bm.pageIndex) }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            modifier = Modifier.weight(1f),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Bookmark,
                                                contentDescription = null,
                                                tint = EditorialCrimson,
                                                modifier = Modifier
                                                    .padding(end = 10.dp)
                                                    .size(20.dp)
                                            )
                                            Column {
                                                Text(
                                                    text = bm.moduleTitle,
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = bm.pageTitle,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                        Text(
                                            text = "OPEN ›",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                            color = EditorialCrimson
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (selectedFilter != LibraryFilter.BOOKMARKS) {
            item(span = { GridItemSpan(2) }) {
                EditorialSectionHeader(
                    title = if (searchQuery.isNotBlank()) "Matching Modules" else "Modules Catalog",
                    indexLabel = if (filteredBookmarks.isNotEmpty() && selectedFilter == LibraryFilter.ALL && searchQuery.isBlank()) "02" else "01"
                )
            }

            if (filteredModules.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Soft3DCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("library_search_empty_state"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SearchOff,
                                contentDescription = null,
                                tint = EditorialCrimson,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No modules found matching \"$searchQuery\"",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Try searching for key journalism concepts:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                suggestedSearches.forEach { term ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.25.dp, EditorialInk),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        modifier = Modifier.clickable { searchQuery = term }
                                    ) {
                                        Text(
                                            text = term,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                items(filteredModules) { module ->
                    val matchResult = searchResults?.firstOrNull { it.module.id == module.id }
                    Column {
                        BookSpineCard(
                            module = module,
                            onClick = { onOpenModule(module.id, 0) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (matchResult != null && !matchResult.matchedSnippet.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            LiquidGlassBox(
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = matchResult.matchedSnippet,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        lineHeight = 13.sp,
                                        fontFamily = FontFamily.Serif
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
