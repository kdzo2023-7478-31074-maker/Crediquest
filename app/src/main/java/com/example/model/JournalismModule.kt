package com.example.model

data class ModulePage(
    val heading: String? = null,
    val paragraphs: List<String> = emptyList(),
    val quote: String? = null,
    val isQuiz: Boolean = false,
    val quizTag: String? = null,
    val question: String? = null,
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0,
    val explanation: String? = null
)

data class JournalismModule(
    val id: Int,
    val moduleNumber: Int,
    val title: String,
    val description: String,
    val pageCount: Int,
    val colorHex: String,
    val keyTopics: List<String>,
    val progressPct: Int = 0,
    val isCompleted: Boolean = false,
    val pages: List<ModulePage> = emptyList()
)

data class BookmarkItem(
    val moduleIndex: Int,
    val pageIndex: Int,
    val moduleTitle: String,
    val pageTitle: String
)

data class QuestChallenge(
    val id: String,
    val category: String,
    val title: String,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val associatedModuleIndex: Int,
    val xpReward: Int,
    val selectedIndex: Int? = null
) {
    val isAnswered: Boolean get() = selectedIndex != null
    val isCorrect: Boolean get() = selectedIndex == correctIndex
}

data class ReaderStats(
    val completedModules: Int = 0,
    val totalModules: Int = 11,
    val answeredQuizzes: Int = 0,
    val correctQuizzes: Int = 0,
    val totalQuizzes: Int = 4,
    val bookmarksCount: Int = 0,
    val currentReadingModuleIndex: Int = 0,
    val currentReadingPageIndex: Int = 0,
    val theme: String = "paper",
    val fontSize: Int = 17
) {
    val accuracyPercentage: Int
        get() = if (answeredQuizzes > 0) ((correctQuizzes.toFloat() / answeredQuizzes) * 100).toInt() else 0

    val overallReadingPercentage: Int
        get() = ((completedModules.toFloat() / totalModules) * 100).toInt().coerceIn(0, 100)
}

data class StudentProfile(
    val name: String = "Student Journalist",
    val institution: String = "Bicol University Polangui",
    val course: String = "Eng Ed 23 (Campus Journalism)",
    val pressAffiliation: String = "The Torchbearer Student Publication",
    val streakDays: Int = 7,
    val rankTitle: String = "Staff Reporter",
    val autoBookmark: Boolean = true,
    val hapticFeedback: Boolean = true,
    val readerTheme: String = "paper"
)

object ModuleCatalog {
    val modules: List<JournalismModule> = listOf(
        JournalismModule(
            id = 0,
            moduleNumber = 1,
            title = "Overview of Campus Journalism",
            description = "Explore Republic Act No. 7079, the role of the campus press, and core ethical standards.",
            pageCount = 3,
            colorHex = "#2F4A5A",
            keyTopics = listOf("R.A. 7079", "Student Press Freedom", "Press Roles"),
            pages = listOf(
                ModulePage(
                    heading = "What is Campus Journalism?",
                    paragraphs = listOf(
                        "Campus journalism is the practice of gathering, writing, editing and publishing information for and by students within a school community.",
                        "Under Republic Act No. 7079, the Campus Journalism Act of 1991, the State promotes the development and growth of campus journalism and the skills of student journalists.",
                        "In this module you will learn who a campus journalist is, what the campus paper is for, and the values that guide the work."
                    )
                ),
                ModulePage(
                    heading = "The role of the campus press",
                    paragraphs = listOf(
                        "A campus paper informs, educates, and gives voice to students. It reports school events, explains issues, and holds a space for opinion.",
                        "Good campus journalism is accurate, fair, and useful to its readers."
                    )
                ),
                ModulePage(
                    isQuiz = true,
                    quizTag = "Quick check",
                    question = "Which law promotes the development of campus journalism in the Philippines?",
                    options = listOf("R.A. 10533", "R.A. 7079", "R.A. 9710", "R.A. 10173"),
                    correctIndex = 1,
                    explanation = "R.A. 7079, the Campus Journalism Act of 1991, supports student journalists and campus publications."
                )
            )
        ),
        JournalismModule(
            id = 1,
            moduleNumber = 2,
            title = "Pre-Writing Concerns",
            description = "Identify newsworthy angles, cross-check secondary tips, and prepare authoritative interview questions.",
            pageCount = 2,
            colorHex = "#6B3B3B",
            keyTopics = listOf("Newsworthiness", "Source Verification", "Angle Selection"),
            pages = listOf(
                ModulePage(
                    heading = "Before you write",
                    paragraphs = listOf(
                        "Strong stories begin before the first sentence. Choose a newsworthy angle, identify your sources, and plan your questions.",
                        "Newsworthiness is measured by timeliness, proximity, prominence, impact, and human interest."
                    )
                ),
                ModulePage(
                    isQuiz = true,
                    quizTag = "Source verification",
                    question = "A classmate tells you the school will cancel classes tomorrow. What is your best next step?",
                    options = listOf(
                        "Publish it right away",
                        "Confirm with the school administration",
                        "Ask two more classmates",
                        "Wait for others to publish first"
                    ),
                    correctIndex = 1,
                    explanation = "Verify with an official, primary source before publishing. Repeating what others say is not verification."
                )
            )
        ),
        JournalismModule(
            id = 2,
            moduleNumber = 3,
            title = "Campus News Writing",
            description = "Master the 5Ws and 1H, craft compelling lead paragraphs, and write inverted pyramid structures.",
            pageCount = 4,
            colorHex = "#3D5A45",
            keyTopics = listOf("5Ws and 1H", "Inverted Pyramid", "Direct Quotes", "Lead Selection"),
            pages = listOf(
                ModulePage(
                    heading = "The 5Ws and 1H",
                    paragraphs = listOf(
                        "Every news story answers six questions: Who, What, When, Where, Why, and How.",
                        "The lead, or first paragraph, carries the most important of these answers in one clear sentence."
                    )
                ),
                ModulePage(
                    heading = "The inverted pyramid",
                    paragraphs = listOf(
                        "Place the most important facts first, then supporting details, then background.",
                        "Readers may stop at any point, so the story must still make sense when cut from the bottom.",
                        "Example lead:"
                    ),
                    quote = "“Bicol University Polangui students will pay no new fees next semester, the SSG announced Tuesday.”"
                ),
                ModulePage(
                    isQuiz = true,
                    quizTag = "Activity: choose the strongest lead",
                    question = "Which is the best news lead?",
                    options = listOf(
                        "The Supreme Student Government held a meeting yesterday.",
                        "Bicol University Polangui students will pay no new fees next semester, the SSG announced Tuesday.",
                        "There was a very important announcement today that students may like.",
                        "Students, listen up: fees are a hot topic!"
                    ),
                    correctIndex = 1,
                    explanation = "It states who, what, and when in a single direct sentence and leads with the news."
                ),
                ModulePage(
                    isQuiz = true,
                    quizTag = "Ethics scenario",
                    question = "A source asks you to remove a direct quote that is accurate and on the record. What should you do?",
                    options = listOf(
                        "Remove it to keep the peace",
                        "Keep it and discuss accuracy with your editor",
                        "Change the wording so it sounds better",
                        "Publish without telling the source"
                    ),
                    correctIndex = 1,
                    explanation = "Accurate quotations stay. Consult your editor and explain your reasoning to the source."
                )
            )
        ),
        JournalismModule(
            id = 3,
            moduleNumber = 4,
            title = "Feature and Science Writing",
            description = "Translate scientific data into plain language and tell compelling human interest stories.",
            pageCount = 1,
            colorHex = "#5A4A2F",
            keyTopics = listOf("Human Interest", "Science Translation", "Narrative Hook"),
            pages = listOf(
                ModulePage(
                    heading = "Telling a human story",
                    paragraphs = listOf(
                        "A feature explores people, trends, or ideas in depth. Its lead may be a scene, a quote, or a question.",
                        "Science writing translates technical findings into plain, accurate language for general readers."
                    )
                )
            )
        ),
        JournalismModule(
            id = 4,
            moduleNumber = 5,
            title = "Editorial and Opinion Writing",
            description = "Construct reasoned arguments, advance school public interest, and provide actionable solutions.",
            pageCount = 1,
            colorHex = "#4A3D5A",
            keyTopics = listOf("Editorial Stance", "Persuasive Evidence", "Call to Action"),
            pages = listOf(
                ModulePage(
                    heading = "Taking a stand",
                    paragraphs = listOf(
                        "An editorial presents the staff’s reasoned position on an issue. It states the issue, argues a view with evidence, and ends with a solution or call to action."
                    )
                )
            )
        ),
        JournalismModule(
            id = 5,
            moduleNumber = 6,
            title = "Sports Writing",
            description = "Capture game highlights, action verbs, statistical accuracy, and player profiles.",
            pageCount = 1,
            colorHex = "#2F5A56",
            keyTopics = listOf("Game Anatomy", "Statistical Veracity", "Action Verbs"),
            pages = listOf(
                ModulePage(
                    heading = "Beyond the score",
                    paragraphs = listOf(
                        "A sports story reports the result, key plays, and the people behind them. Accuracy of names, scores, and statistics matters most."
                    )
                )
            )
        ),
        JournalismModule(
            id = 6,
            moduleNumber = 7,
            title = "Revising, Editing and Copyreading",
            description = "Polish copy for grammatical precision, AP/DepEd stylebook compliance, and write punchy headlines.",
            pageCount = 1,
            colorHex = "#5A3D4D",
            keyTopics = listOf("Copyediting Symbols", "Headline Writing", "Fact Checking"),
            pages = listOf(
                ModulePage(
                    heading = "Polishing the copy",
                    paragraphs = listOf(
                        "Revise for structure and clarity, edit for style, and proofread for grammar, spelling, and mechanics.",
                        "Copyreaders also write headlines and check facts against the source."
                    )
                )
            )
        ),
        JournalismModule(
            id = 7,
            moduleNumber = 8,
            title = "Photojournalism",
            description = "Learn picture reporting techniques, rule of thirds, and write complete cutlines.",
            pageCount = 1,
            colorHex = "#44505A",
            keyTopics = listOf("Visual Storytelling", "Caption Composition", "Ethics of Framing"),
            pages = listOf(
                ModulePage(
                    heading = "Pictures that report",
                    paragraphs = listOf(
                        "A news photo tells a story on its own. Good captions identify who, what, when, and where."
                    )
                )
            )
        ),
        JournalismModule(
            id = 8,
            moduleNumber = 9,
            title = "Cartooning and Lay-outing",
            description = "Use visual satire and metaphors, and design balanced publication page structures.",
            pageCount = 1,
            colorHex = "#5A5A2F",
            keyTopics = listOf("Editorial Cartoons", "Page Grid", "Visual Hierarchy"),
            pages = listOf(
                ModulePage(
                    heading = "Visual communication",
                    paragraphs = listOf(
                        "Editorial cartoons make a point through symbols and humor. Layout guides the reader’s eye through headlines, photos, and text."
                    )
                )
            )
        ),
        JournalismModule(
            id = 9,
            moduleNumber = 10,
            title = "Script Writing and Newscasting",
            description = "Write broadcast copy for the ear, pace voice delivery, and assemble cue sheets.",
            pageCount = 1,
            colorHex = "#3A3A3A",
            keyTopics = listOf("Writing for the Ear", "Broadcast Pacing", "Voice Inflection"),
            pages = listOf(
                ModulePage(
                    heading = "Writing for the ear",
                    paragraphs = listOf(
                        "Broadcast copy is short, conversational, and written to be heard. Read every line aloud before you air it."
                    )
                )
            )
        ),
        JournalismModule(
            id = 10,
            moduleNumber = 11,
            title = "Desktop Publishing",
            description = "Bring text, imagery, and typography together into press-ready digital or print layouts.",
            pageCount = 1,
            colorHex = "#5A3A2A",
            keyTopics = listOf("Typography", "Prepress Prep", "Digital Distribution"),
            pages = listOf(
                ModulePage(
                    heading = "From draft to print",
                    paragraphs = listOf(
                        "Desktop publishing combines text, images, and layout into a finished publication ready for print or online release."
                    )
                )
            )
        )
    )

    val sampleQuests: List<QuestChallenge> = listOf(
        QuestChallenge(
            id = "quest_ra7079",
            category = "Legal Framework",
            title = "Campus Journalism Act of 1991",
            prompt = "Which law promotes the development and growth of campus journalism in the Philippines?",
            options = listOf("R.A. 10533", "R.A. 7079", "R.A. 9710", "R.A. 10173"),
            correctIndex = 1,
            explanation = "R.A. 7079, the Campus Journalism Act of 1991, provides the legal foundation for student journalism in the Philippines.",
            associatedModuleIndex = 0,
            xpReward = 50
        ),
        QuestChallenge(
            id = "quest_verification",
            category = "Source Verification",
            title = "Class Suspension Tip",
            prompt = "A classmate tells you the school will cancel classes tomorrow. What is your best next step?",
            options = listOf("Publish it right away", "Confirm with the school administration", "Ask two more classmates", "Wait for others to publish first"),
            correctIndex = 1,
            explanation = "Always verify with an official, primary source before publishing. Repeating rumors is not verification.",
            associatedModuleIndex = 1,
            xpReward = 50
        ),
        QuestChallenge(
            id = "quest_lead",
            category = "News Writing",
            title = "Strongest News Lead",
            prompt = "Which lead sentence best captures the 5Ws and 1H for a campus newspaper?",
            options = listOf(
                "The Supreme Student Government held a meeting yesterday.",
                "Bicol University Polangui students will pay no new fees next semester, the SSG announced Tuesday.",
                "There was a very important announcement today that students may like.",
                "Students, listen up: fees are a hot topic!"
            ),
            correctIndex = 1,
            explanation = "It states who, what, and when in a single direct sentence and leads with the most newsworthy impact.",
            associatedModuleIndex = 2,
            xpReward = 60
        ),
        QuestChallenge(
            id = "quest_ethics",
            category = "Journalism Ethics",
            title = "On-the-Record Quote Request",
            prompt = "A source asks you to remove a direct quote that is accurate and was given on the record. What should you do?",
            options = listOf(
                "Remove it immediately to keep the peace",
                "Keep it and discuss accuracy with your editor",
                "Change the wording so it sounds better",
                "Publish without telling the source"
            ),
            correctIndex = 1,
            explanation = "Accurate on-the-record quotations stand. Consult your editor and explain the journalistic rationale to the source.",
            associatedModuleIndex = 2,
            xpReward = 75
        )
    )
}

data class SearchMatchResult(
    val module: JournalismModule,
    val matchedInTitle: Boolean,
    val matchedInTopics: List<String>,
    val matchedSnippet: String?
)

fun JournalismModule.matchesQuery(query: String): SearchMatchResult? {
    val q = query.trim()
    if (q.isEmpty()) return null

    val inTitle = title.contains(q, ignoreCase = true)
    val inDesc = description.contains(q, ignoreCase = true)
    val matchedTopics = keyTopics.filter { it.contains(q, ignoreCase = true) }

    var snippet: String? = null

    // Search across all pages within the module content
    for (page in pages) {
        if (page.heading?.contains(q, ignoreCase = true) == true) {
            snippet = "Section: ${page.heading}"
            break
        }
        val matchedPara = page.paragraphs.firstOrNull { it.contains(q, ignoreCase = true) }
        if (matchedPara != null) {
            snippet = extractSnippet(matchedPara, q)
            break
        }
        if (page.quote?.contains(q, ignoreCase = true) == true) {
            snippet = "Quote: \"${page.quote}\""
            break
        }
        if (page.question?.contains(q, ignoreCase = true) == true) {
            snippet = "Quiz: ${page.question}"
            break
        }
        val matchedOption = page.options.firstOrNull { it.contains(q, ignoreCase = true) }
        if (matchedOption != null) {
            snippet = "Option: $matchedOption"
            break
        }
        if (page.explanation?.contains(q, ignoreCase = true) == true) {
            snippet = "Note: ${page.explanation}"
            break
        }
    }

    if (inTitle || inDesc || matchedTopics.isNotEmpty() || snippet != null) {
        return SearchMatchResult(
            module = this,
            matchedInTitle = inTitle,
            matchedInTopics = matchedTopics,
            matchedSnippet = snippet ?: if (inDesc) description else null
        )
    }
    return null
}

private fun extractSnippet(text: String, query: String, maxLength: Int = 110): String {
    val idx = text.indexOf(query, ignoreCase = true)
    if (idx < 0) return text.take(maxLength)
    val start = (idx - 25).coerceAtLeast(0)
    val end = (idx + query.length + 55).coerceAtMost(text.length)
    val prefix = if (start > 0) "…" else ""
    val suffix = if (end < text.length) "…" else ""
    return "$prefix${text.substring(start, end).trim()}$suffix"
}
