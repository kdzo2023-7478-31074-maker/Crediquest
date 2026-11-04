package com.example.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.model.JournalismModule
import com.example.model.ModulePage
import com.example.ui.theme.DarkLine
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.LineBorder
import com.example.ui.theme.PaperLight
import com.example.ui.theme.SepiaLine
import com.example.ui.theme.SepiaPaper

class WebAppBridge(private val onMessageReceived: (String) -> Unit) {
    @JavascriptInterface
    fun postMessage(jsonString: String) {
        onMessageReceived(jsonString)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ReaderScreen(
    moduleIndex: Int,
    pageIndex: Int,
    modules: List<JournalismModule>,
    isCurrentBookmarked: Boolean,
    currentTheme: String,
    fontSize: Int,
    onBridgeMessage: (String) -> Unit,
    onCloseReader: () -> Unit,
    modifier: Modifier = Modifier
) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var isWebLoaded by remember { mutableStateOf(false) }
    var hasRendererCrashed by remember { mutableStateOf(false) }
    var useNativeReader by remember { mutableStateOf(false) }
    var showDisplaySheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    var activePage by remember(pageIndex) { mutableIntStateOf(pageIndex) }
    val currentModule = modules.getOrNull(moduleIndex) ?: modules.first()
    val totalPages = currentModule.pages.size.coerceAtLeast(1)

    // Hardware back navigation handler
    BackHandler {
        if (useNativeReader || hasRendererCrashed) {
            if (activePage > 0) {
                activePage--
            } else {
                onCloseReader()
            }
        } else {
            webViewInstance?.evaluateJavascript("window.nativeGoBack();") { result ->
                if (result != "true") {
                    onCloseReader()
                }
            } ?: onCloseReader()
        }
    }

    // Dynamic reader colors based on selected theme
    val readerBgColor = when (currentTheme) {
        "sepia" -> Color(0xFFF1E6CF)
        "dark" -> Color(0xFF17171A)
        else -> Color(0xFFF5F3EE)
    }

    val readerTextColor = when (currentTheme) {
        "sepia" -> Color(0xFF3B2F1E)
        "dark" -> Color(0xFFEDEAE1)
        else -> Color(0xFF1C1B19)
    }

    val readerMuteColor = when (currentTheme) {
        "sepia" -> Color(0xFF7A6A50)
        "dark" -> Color(0xFF8D8A82)
        else -> Color(0xFF6F6B63)
    }

    val readerLineColor = when (currentTheme) {
        "sepia" -> Color(0xFFCDBB98)
        "dark" -> Color(0xFF3A3A3F)
        else -> Color(0xFFBDB8AD)
    }

    val readerHighlightColor = when (currentTheme) {
        "sepia" -> Color(0xFFE3D3AE)
        "dark" -> Color(0xFF2B2B30)
        else -> Color(0xFFE4DFD2)
    }

    // When module/page index changes, instruct web view
    LaunchedEffect(moduleIndex, activePage, isWebLoaded) {
        if (isWebLoaded && !hasRendererCrashed) {
            webViewInstance?.evaluateJavascript("window.nativeOpenModule($moduleIndex, $activePage);", null)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(readerBgColor)
    ) {
        // Native Top Control Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars),
            color = readerBgColor,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (useNativeReader || hasRendererCrashed) {
                            if (activePage > 0) activePage-- else onCloseReader()
                        } else {
                            webViewInstance?.evaluateJavascript("window.nativeGoBack();") { result ->
                                if (result != "true") onCloseReader()
                            } ?: onCloseReader()
                        }
                    },
                    modifier = Modifier.testTag("reader_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = readerTextColor
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentModule.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = readerTextColor
                    )
                    Text(
                        text = "Module ${currentModule.moduleNumber} · Page ${activePage + 1} of $totalPages",
                        style = MaterialTheme.typography.labelSmall,
                        color = readerMuteColor
                    )
                }

                // Switch between Web and Native mode if desired
                IconButton(
                    onClick = { useNativeReader = !useNativeReader },
                    modifier = Modifier.testTag("reader_switch_mode_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.SwapHoriz,
                        contentDescription = if (useNativeReader) "Switch to Web Reader" else "Switch to Native Reader",
                        tint = readerTextColor
                    )
                }

                IconButton(
                    onClick = { showDisplaySheet = true },
                    modifier = Modifier.testTag("reader_appearance_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.FormatSize,
                        contentDescription = "Appearance Settings",
                        tint = readerTextColor
                    )
                }

                IconButton(
                    onClick = {
                        if (useNativeReader || hasRendererCrashed) {
                            // Toggle bookmark in native
                            onBridgeMessage(
                                """{"action":"bookmark_toggled","payload":{"moduleIndex":$moduleIndex,"pageIndex":$activePage,"isBookmarked":${!isCurrentBookmarked},"moduleTitle":"${currentModule.title}"}}"""
                            )
                        } else {
                            webViewInstance?.evaluateJavascript("window.nativeToggleBookmark();", null)
                        }
                    },
                    modifier = Modifier.testTag("reader_bookmark_button")
                ) {
                    Icon(
                        imageVector = if (isCurrentBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = "Bookmark Page",
                        tint = if (isCurrentBookmarked) MaterialTheme.colorScheme.primary else readerMuteColor
                    )
                }
            }
        }

        // Reading Content: Either Software WebView or Native Compose Reader
        Box(modifier = Modifier.weight(1f)) {
            if (!useNativeReader && !hasRendererCrashed) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("embedded_html_webview"),
                    factory = { context ->
                        WebView(context).apply {
                            // Critical: Force software layer to prevent Chromium GPU renderer crash (code -1) in emulators
                            setLayerType(View.LAYER_TYPE_SOFTWARE, null)

                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                allowFileAccess = false
                                allowContentAccess = false
                                loadWithOverviewMode = true
                                useWideViewPort = true
                                cacheMode = WebSettings.LOAD_NO_CACHE
                            }

                            addJavascriptInterface(WebAppBridge { msg ->
                                post { onBridgeMessage(msg) }
                            }, "AndroidBridge")

                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isWebLoaded = true
                                    view?.evaluateJavascript(
                                        "window.nativeOpenModule($moduleIndex, $activePage);",
                                        null
                                    )
                                }

                                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                    return false
                                }

                                // Essential for handling renderer process crash without terminating host application!
                                override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
                                    view?.destroy()
                                    hasRendererCrashed = true
                                    useNativeReader = true
                                    return true
                                }
                            }

                            try {
                                val htmlData = context.assets.open("www/index.html").bufferedReader().use { it.readText() }
                                loadDataWithBaseURL("file:///android_asset/www/", htmlData, "text/html", "UTF-8", null)
                            } catch (e: Exception) {
                                loadUrl("file:///android_asset/www/index.html")
                            }

                            webViewInstance = this
                        }
                    },
                    update = { webView ->
                        webViewInstance = webView
                    }
                )

                if (!isWebLoaded) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(readerBgColor),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = readerTextColor,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            } else {
                // Native Compose Editorial Reader (Ultra-stable, zero-crash fallback and reading mode)
                val currentPageData: ModulePage = currentModule.pages.getOrNull(activePage)
                    ?: ModulePage(
                        heading = currentModule.title,
                        paragraphs = listOf(currentModule.description)
                    )

                var selectedQuizAnswer by remember(activePage, moduleIndex) { mutableStateOf<Int?>(null) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(readerBgColor)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (currentPageData.isQuiz) {
                            // Quiz View
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, readerTextColor),
                                color = readerBgColor,
                                modifier = Modifier.padding(bottom = 12.dp)
                            ) {
                                Text(
                                    text = currentPageData.quizTag ?: "Quick check",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                    color = readerTextColor,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                )
                            }

                            Text(
                                text = currentPageData.question ?: "",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = (fontSize + 1).sp,
                                    lineHeight = (fontSize + 8).sp
                                ),
                                color = readerTextColor,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            currentPageData.options.forEachIndexed { optIdx, optText ->
                                val isSelected = selectedQuizAnswer == optIdx
                                val isCorrectOption = currentPageData.correctIndex == optIdx

                                val optBgColor = when {
                                    selectedQuizAnswer != null && isCorrectOption -> readerTextColor
                                    selectedQuizAnswer != null && isSelected && !isCorrectOption -> readerHighlightColor
                                    else -> readerBgColor
                                }

                                val optTextColor = when {
                                    selectedQuizAnswer != null && isCorrectOption -> readerBgColor
                                    else -> readerTextColor
                                }

                                val optBorderColor = when {
                                    selectedQuizAnswer != null && isCorrectOption -> readerTextColor
                                    selectedQuizAnswer != null && isSelected && !isCorrectOption -> readerTextColor
                                    else -> readerLineColor
                                }

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp)
                                        .clickable(enabled = selectedQuizAnswer == null) {
                                            selectedQuizAnswer = optIdx
                                            val isCorrect = optIdx == currentPageData.correctIndex
                                            onBridgeMessage(
                                                """{"action":"quiz_answered","payload":{"moduleIndex":$moduleIndex,"pageIndex":$activePage,"choiceIndex":$optIdx,"isCorrect":$isCorrect}}"""
                                            )
                                        },
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.5.dp, optBorderColor),
                                    color = optBgColor
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${('A' + optIdx)}. $optText",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontSize = fontSize.sp,
                                                lineHeight = (fontSize + 6).sp,
                                                fontWeight = if (selectedQuizAnswer != null && isCorrectOption) FontWeight.Bold else FontWeight.Normal
                                            ),
                                            color = optTextColor,
                                            modifier = Modifier.weight(1f)
                                        )

                                        if (selectedQuizAnswer != null) {
                                            if (isCorrectOption) {
                                                Icon(
                                                    imageVector = Icons.Filled.CheckCircle,
                                                    contentDescription = "Correct",
                                                    tint = optTextColor,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            } else if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Filled.Close,
                                                    contentDescription = "Incorrect",
                                                    tint = readerTextColor,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Feedback box
                            AnimatedVisibility(visible = selectedQuizAnswer != null) {
                                val isCorrect = selectedQuizAnswer == currentPageData.correctIndex
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(readerHighlightColor)
                                        .padding(14.dp)
                                ) {
                                    Text(
                                        text = if (isCorrect) "Correct." else "Not quite.",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = readerTextColor
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = currentPageData.explanation ?: "",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = (fontSize - 1).sp,
                                            lineHeight = (fontSize + 5).sp
                                        ),
                                        color = readerTextColor
                                    )
                                }
                            }
                        } else {
                            // Standard Reading Passages
                            if (!currentPageData.heading.isNullOrBlank()) {
                                Text(
                                    text = currentPageData.heading,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = (fontSize + 4).sp,
                                        lineHeight = (fontSize + 10).sp
                                    ),
                                    color = readerTextColor,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }

                            currentPageData.paragraphs.forEach { paragraph ->
                                Text(
                                    text = paragraph,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = FontFamily.Serif,
                                        fontSize = fontSize.sp,
                                        lineHeight = (fontSize * 1.65).sp
                                    ),
                                    color = readerTextColor,
                                    textAlign = TextAlign.Justify,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }

                            if (!currentPageData.quote.isNullOrBlank()) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    color = readerHighlightColor,
                                    shape = RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(4.dp)
                                                .height(48.dp)
                                                .background(readerTextColor)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = currentPageData.quote,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontFamily = FontFamily.Serif,
                                                fontStyle = FontStyle.Italic,
                                                fontSize = fontSize.sp,
                                                lineHeight = (fontSize * 1.5).sp
                                            ),
                                            color = readerTextColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Bottom Navigation Buttons inside Native Reader
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (activePage > 0) {
                                    activePage--
                                } else {
                                    onCloseReader()
                                }
                            },
                            enabled = true,
                            border = BorderStroke(1.dp, readerTextColor),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = readerTextColor)
                        ) {
                            Text(if (activePage > 0) "‹ Previous" else "‹ Library")
                        }

                        Text(
                            text = "${activePage + 1} / $totalPages",
                            style = MaterialTheme.typography.labelSmall,
                            color = readerMuteColor
                        )

                        Button(
                            onClick = {
                                if (activePage < totalPages - 1) {
                                    activePage++
                                } else {
                                    onCloseReader()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = readerTextColor,
                                contentColor = readerBgColor
                            )
                        ) {
                            Text(if (activePage < totalPages - 1) "Next ›" else "Finish")
                        }
                    }
                }
            }
        }

        // Bottom Progress Bar
        val readProgressPct = ((activePage + 1).toFloat() / totalPages).coerceIn(0f, 1f)
        LinearProgressIndicator(
            progress = { readProgressPct },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp),
            color = readerTextColor,
            trackColor = readerLineColor
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(readerBgColor)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Module ${currentModule.moduleNumber} · Page ${activePage + 1} of $totalPages",
                style = MaterialTheme.typography.labelSmall,
                color = readerMuteColor
            )
            Text(
                text = "${(readProgressPct * 100).toInt()}% read",
                style = MaterialTheme.typography.labelSmall,
                color = readerMuteColor
            )
        }
    }

    // Appearance Bottom Sheet
    if (showDisplaySheet) {
        ModalBottomSheet(
            onDismissRequest = { showDisplaySheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Reading Display Settings",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Theme selection
                Text(
                    text = "Background Theme",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("paper" to "Paper", "sepia" to "Sepia", "dark" to "Dark").forEach { (thKey, thLabel) ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    webViewInstance?.evaluateJavascript("window.nativeSetTheme('$thKey');", null)
                                    onBridgeMessage("""{"action":"theme_changed","payload":{"theme":"$thKey"}}""")
                                },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(
                                width = if (currentTheme == thKey) 2.dp else 1.dp,
                                color = if (currentTheme == thKey) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            ),
                            color = when (thKey) {
                                "sepia" -> Color(0xFFF1E6CF)
                                "dark" -> Color(0xFF17171A)
                                else -> Color(0xFFF5F3EE)
                            }
                        ) {
                            Text(
                                text = thLabel,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (thKey == "dark") Color.White else Color.Black,
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Font size buttons
                Text(
                    text = "Text Size ($fontSize px)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                webViewInstance?.evaluateJavascript("window.nativeSetFontSize(-1);", null)
                                onBridgeMessage("""{"action":"font_size_changed","payload":{"delta":-1}}""")
                            },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Text(
                            text = "Smaller (A−)",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(vertical = 12.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                webViewInstance?.evaluateJavascript("window.nativeSetFontSize(1);", null)
                                onBridgeMessage("""{"action":"font_size_changed","payload":{"delta":1}}""")
                            },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Text(
                            text = "Larger (A+)",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(vertical = 12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
