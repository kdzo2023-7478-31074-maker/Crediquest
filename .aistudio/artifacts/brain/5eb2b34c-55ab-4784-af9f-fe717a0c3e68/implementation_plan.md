# CrediQuest – Campus Journalism Reader & Study Companion

CrediQuest wraps the interactive campus journalism reader (Eng Ed 23 supplementary learning modules) into a native Android application using a Jetpack Compose shell. It features four primary navigation tabs (Home, Library, Quests, and Profile), seamless offline WebView integration with JavaScript-native bridging, and persistent reading preferences and student statistics.

### User Review & Critical Decisions

> [!IMPORTANT]
> The architectural direction and scope have been aligned based on your selections:

- **Confirmed Decision 1 (Integration Architecture)**: Hybrid Jetpack Compose native shell hosting the responsive HTML reader inside a hardened Android `WebView`, bridged to native state. This preserves the interactive typography, custom animations, and quizzes of the HTML while providing native Android navigation, edge-to-edge system insets, and hardware back-button handling.
- **Confirmed Decision 2 (Navigation Tabs)**: Bottom navigation featuring **Home** (dashboard & quick resume), **Library** (catalog of 11 journalism modules & bookmarks), **Quests** (challenges, quick checks, and ethical dilemma exercises), and **Profile** (student identity, reading stats, and reader preferences).
- **Confirmed Decision 3 (Profile & Settings)**: Dedicated profile screen showcasing reading statistics (completion percentage, questions answered, accuracy rate), reader preferences (default paper/sepia/dark theme, font scale, auto-bookmarking), and student affiliation (e.g., Bicol University Polangui Eng Ed 23).
- **Default for Uncrafted Modules**: Future modules (specialized journalism workshops and advanced quests) will feature polished editorial placeholder cards with "Coming Soon" badges, module objectives, and syllabus outlines ready for seamless subsequent drop-in.

---

### 1. Overview & Core Concept

- **What It Does**: CrediQuest is a mobile reader and study companion for student journalists studying Republic Act No. 7079 (Campus Journalism Act of 1991), news gathering, lead writing, editorial ethics, and publication design.
- **Target Audience / Persona**: Campus journalists, journalism education students (such as Bicol University Polangui Eng Ed 23), student publication editors, and educators.
- **Key Value**: Delivers an distraction-free, book-like reading experience with instant quiz verification, customizable typography/color themes, and progress tracking across all 11 curriculum modules.

---

### 2. User Experience & Visual Design

#### Key User Flows
1. **Launch & Onboarding**: Animated bookshelf splash screen seamlessly transitions into the native app shell.
2. **Home Tab**: Displays the active module hero card with reading progress percentage, "Resume" action button, overall stats grid, and "Up Next" module carousel.
3. **Library Tab**: Displays the full 11-module library grid with colored spine accents, completion status, and a dedicated Bookmarks section. Tapping any module opens the reader.
4. **Reader Flow**: Full-screen editorial reading interface with side-tap and gesture swipe page navigation, interactive multiple-choice quick checks with instant explanations, Aa appearance pane (paper, sepia, dark theme & font size controls), and quick bookmark toggle.
5. **Quests Tab**: Interactive exercise hub listing quick-check challenges, source verification scenarios, and ethics case studies with score tracking.
6. **Profile & Settings Tab**: Displays student profile header, comprehensive reading statistics (modules mastered, questions solved, accuracy rate), reader display settings, and app version details.

#### Visual Identity & Theme
- **Aesthetic Direction**: Warm editorial / literary press aesthetic inspired by printed campus broadsheets, warm book paper, and clean typography.
- **Color Palette**:
  - `Paper Background`: `#F5F3EE` (Light) / `#17171A` (Dark) / `#F1E6CF` (Sepia)
  - `Ink (Primary)`: `#1C1B19` (Light) / `#DEDB82` (Dark) / `#3B2F1E` (Sepia)
  - `Muted Slate`: `#6F6B63`
  - `Editorial Accent`: Deep bookcloth colors (`#2F4A5A` Navy, `#6B3B3B` Burgundy, `#3D5A45` Forest Green, `#5A4A2F` Amber)
- **Typography**: Editorial serif typography for headings and reading passages (`Georgia`, `Bookerly`, `Times New Roman`) paired with crisp sans-serif (`system-ui`, Roboto) for metadata, stats, and interface pills.
- **Component Styling & Layout**:
  - Edge-to-edge layout with full support for status bar and navigation bar insets.
  - Floating and docked M3 NavigationBar with active indicator pills and distinct icons.
  - Tactile book-spine cards with realistic drop shadows and progress bars.

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Compose WebView Wrapper vs. Rewriting HTML in Pure Compose**
  - *Chosen Approach*: Embed the bundled `crediquest.html` reader in `app/src/main/assets/www/` rendered via Android `WebView` inside a Compose `AndroidView`, complemented by native Compose navigation tabs and screens.
  - *Why*: The provided HTML already implements a complete book engine with fluid typography, responsive layout, CSS bookcloth themes (paper/sepia/dark), touch gesture navigation, and interactive quiz feedback. Embedding preserves 100% of the interactive behaviors with zero latency while allowing native Compose tabs to manage the broader app lifecycle.
  - *Alternative Considered*: Complete rewrite into Compose Canvas/Text components. This would discard existing, tested interactive page turns, CSS transitions, and quiz answer validations without end-user benefit.

- **Decision 2: Local Persistence & State Synchronization**
  - *Chosen Approach*: Two-way bridge via Android `@JavascriptInterface` and Kotlin ViewModel/DataStore preferences.
  - *Why*: When a student completes a module, answers a quiz, or bookmarks a page in the HTML reader, JavaScript notifies the Android ViewModel to update persistent reading statistics shown in the native Home and Profile tabs.

- **Decision 3: Modular Tab Extensibility**
  - *Chosen Approach*: The Quests and Profile screens are structured as native Compose modular screens with state hoisting, ready for backend integration or syllabus expansion.

---

### 4. Technical Architecture & Data Strategy *(Technical Reference)*

#### System Architecture Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                    MainActivity (Compose)                    │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐  │
│  │                    App Scaffold                        │  │
│  │  ┌──────────────────────────────────────────────────┐  │  │
│  │  │                Current Tab Screen                │  │  │
│  │  │                                                  │  │  │
│  │  │  [HomeTab]        [LibraryTab]     [QuestsTab]   │  │  │
│  │  │  • Stats Summary  • 11 Modules     • Ethics      │  │  │
│  │  │  • Resume Card    • Bookmarks      • Quizzes     │  │  │
│  │  │                                                  │  │  │
│  │  │  [ProfileTab]     [EmbeddedReaderScreen]         │  │  │
│  │  │  • Student Info   • Android WebView              │  │  │
│  │  │  • Reading Stats  • file:///android_asset/...    │  │  │
│  │  │  • Reader Prefs   • JavascriptInterface Bridge   │  │  │
│  │  └──────────────────────────────────────────────────┘  │  │
│  │                                                        │  │
│  │  ┌──────────────────────────────────────────────────┐  │  │
│  │  │      M3 NavigationBar (Home/Library/Quests/User) │  │  │
│  │  └──────────────────────────────────────────────────┘  │  │
│  └────────────────────────────────────────────────────────┘  │
│                               ▲                              │
│                               │ StateFlow & Events           │
│                               ▼                              │
│  ┌────────────────────────────────────────────────────────┐  │
│  │                   ReaderViewModel                      │  │
│  │  • Module Progress Tracker   • Preferences Repository  │  │
│  │  • Bookmarks StateFlow       • Quiz History State      │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

#### Key Components & Responsibilities
1. **`ReaderWebView`**: Robust Compose-wrapped `WebView` loading `file:///android_asset/www/index.html` with safe settings (file access restricted to bundled assets, DOM storage enabled, hardware acceleration active).
2. **`WebAppInterface`**: Kotlin bridge annotated with `@JavascriptInterface` exposing progress callbacks (`onPageChanged`, `onQuizAnswered`, `onBookmarkToggled`).
3. **`ReaderViewModel`**: Manages reading state, module metadata, user profile statistics, and visual preferences across screen rotations and tab switches.
4. **Custom App Icon**: Adaptive launcher icon featuring an open book and quill with editorial bookcloth branding replacing the default green Android icon.

---
