package com.quizrevision.smartquizandrevisionapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizrevision.smartquizandrevisionapp.data.SampleData
import com.quizrevision.smartquizandrevisionapp.data.model.Course
import com.quizrevision.smartquizandrevisionapp.data.model.Question
import com.quizrevision.smartquizandrevisionapp.data.model.QuestionSource
import com.quizrevision.smartquizandrevisionapp.data.model.Topic
import com.quizrevision.smartquizandrevisionapp.navigation.Screen
import com.quizrevision.smartquizandrevisionapp.ui.components.AppBottomBar
import com.quizrevision.smartquizandrevisionapp.ui.components.AppCard
import com.quizrevision.smartquizandrevisionapp.ui.components.AppTopBar
import com.quizrevision.smartquizandrevisionapp.ui.components.GhostButton
import com.quizrevision.smartquizandrevisionapp.ui.components.PrimaryButton
import com.quizrevision.smartquizandrevisionapp.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class GenerationState { IDLE, LOADING, SUCCESS, OFFLINE_ERROR }

/**
 * AI Question Generation screen with parameter selectors and primary button styling.
 */
@Composable
fun AIGenerateScreen(
    onAboutClick: () -> Unit,
    onNavBarSelect: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val courses = SampleData.courses
    var selectedCourse by remember { mutableStateOf(courses.first()) }
    val topics = remember(selectedCourse) { SampleData.topicsFor(selectedCourse.id) }
    var selectedTopic by remember { mutableStateOf(topics.firstOrNull() ?: Topic("t_default", selectedCourse.id, "General Practice", 10, 0)) }

    LaunchedEffect(selectedCourse) {
        val newTopics = SampleData.topicsFor(selectedCourse.id)
        selectedTopic = newTopics.firstOrNull() ?: Topic("t_default", selectedCourse.id, "General Practice", 10, 0)
    }

    val difficulties = listOf("Easy", "Medium", "Hard")
    var selectedDifficulty by remember { mutableStateOf("Medium") }

    val counts = listOf("3 questions", "5 questions", "10 questions")
    var selectedCount by remember { mutableStateOf("5 questions") }

    var state by remember { mutableStateOf(GenerationState.IDLE) }
    var generatedQuestions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var savedSuccess by remember { mutableStateOf(false) }

    var courseExpanded by remember { mutableStateOf(false) }
    var topicExpanded by remember { mutableStateOf(false) }
    var difficultyExpanded by remember { mutableStateOf(false) }
    var countExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(
                title = "Generate with AI",
                showAbout = true,
                onAboutClick = onAboutClick
            )
        },
        bottomBar = { AppBottomBar(currentRoute = Screen.AIGenerate.route, onSelect = onNavBarSelect) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.Spacing24, vertical = Dimens.Spacing16)
        ) {
            Text(
                "Generate custom practice questions using AI based on your selected course, topic, and difficulty level. Generated questions are saved locally on your device for offline review and quizzes.",
                color = TextDim,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                modifier = Modifier.padding(bottom = Dimens.Spacing16)
            )

            // Course Selector
            SelectorDropdownRow(
                label = "Course",
                selectedText = selectedCourse.name,
                expanded = courseExpanded,
                onExpandedChange = { courseExpanded = it },
                items = courses,
                itemText = { it.name },
                onItemSelected = { selectedCourse = it; courseExpanded = false }
            )

            // Topic Selector
            SelectorDropdownRow(
                label = "Topic",
                selectedText = selectedTopic.name,
                expanded = topicExpanded,
                onExpandedChange = { topicExpanded = it },
                items = topics,
                itemText = { it.name },
                onItemSelected = { selectedTopic = it; topicExpanded = false }
            )

            // Difficulty Selector
            SelectorDropdownRow(
                label = "Difficulty",
                selectedText = selectedDifficulty,
                expanded = difficultyExpanded,
                onExpandedChange = { difficultyExpanded = it },
                items = difficulties,
                itemText = { it },
                onItemSelected = { selectedDifficulty = it; difficultyExpanded = false }
            )

            // Number of Questions Selector
            SelectorDropdownRow(
                label = "Number of questions",
                selectedText = selectedCount,
                expanded = countExpanded,
                onExpandedChange = { countExpanded = it },
                items = counts,
                itemText = { it },
                onItemSelected = { selectedCount = it; countExpanded = false }
            )

            Spacer(Modifier.height(Dimens.Spacing8))

            PrimaryButton(
                text = if (state == GenerationState.LOADING) "Generating AI Questions…" else "Generate questions",
                onClick = {
                    if (state != GenerationState.LOADING) {
                        state = GenerationState.LOADING
                        savedSuccess = false
                        scope.launch {
                            val countInt = selectedCount.takeWhile { it.isDigit() }.toIntOrNull() ?: 5
                            delay(1400)
                            generatedQuestions = generateAiQuestions(selectedTopic.id, selectedTopic.name, selectedDifficulty, countInt)
                            state = GenerationState.SUCCESS
                        }
                    }
                },
                modifier = Modifier.padding(bottom = Dimens.Spacing20),
                leadingIcon = { Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp)) }
            )

            when (state) {
                GenerationState.SUCCESS -> {
                    GeneratedQuestionPreviewList(
                        questions = generatedQuestions,
                        savedSuccess = savedSuccess,
                        onSave = {
                            SampleData.addAiQuestions(selectedTopic.id, generatedQuestions)
                            savedSuccess = true
                            scope.launch {
                                snackbarHostState.showSnackbar("Successfully saved ${generatedQuestions.size} questions to local storage for '${selectedTopic.name}'!")
                            }
                        },
                        onDiscard = {
                            state = GenerationState.IDLE
                            generatedQuestions = emptyList()
                        }
                    )
                }
                GenerationState.OFFLINE_ERROR -> OfflineBanner()
                GenerationState.LOADING -> {
                    Box(modifier = Modifier.fillMaxWidth().padding(Dimens.Spacing32), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Accent)
                    }
                }
                GenerationState.IDLE -> { /* nothing yet */ }
            }

            Spacer(Modifier.height(Dimens.Spacing24))

            AppCard(backgroundColor = AppSurface) {
                Text(
                    "SECURITY & ARCHITECTURE NOTE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    modifier = Modifier.padding(bottom = Dimens.Spacing4)
                )
                Text(
                    "API keys are never exposed in client source code. For production LLM integration, requests route through a secure backend proxy (e.g., Ktor / Node.js) holding credentials safely as environment variables.",
                    fontSize = 12.sp,
                    color = TextDim,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun <T> SelectorDropdownRow(
    label: String,
    selectedText: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<T>,
    itemText: (T) -> String,
    onItemSelected: (T) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = Dimens.Spacing12)) {
        Text(
            label.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextDim,
            modifier = Modifier.padding(bottom = Dimens.Spacing4)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.CornerRadius12))
                .background(AppSurface)
                .border(1.5.dp, Border, RoundedCornerShape(Dimens.CornerRadius12))
                .clickable { onExpandedChange(!expanded) }
                .padding(Dimens.Spacing16)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(selectedText, fontWeight = FontWeight.SemiBold, fontSize = 14.5.sp, color = TextPrimary)
                Icon(Icons.Filled.ExpandMore, contentDescription = null, tint = TextDim, modifier = Modifier.size(18.dp))
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.background(AppSurface)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(itemText(item), color = TextPrimary, fontWeight = FontWeight.Medium) },
                        onClick = { onItemSelected(item) }
                    )
                }
            }
        }
    }
}

private fun generateAiQuestions(topicId: String, topicName: String, difficulty: String, count: Int): List<Question> {
    val samplePool = listOf(
        Question(
            id = "ai_q_${System.currentTimeMillis()}_1",
            topicId = topicId,
            text = "In $topicName ($difficulty level), which core principle ensures robust error handling and maintainability?",
            options = listOf("Separation of concerns & structured state", "Hardcoding all logic in UI views", "Ignoring exceptions silently", "Using global mutable singletons"),
            correctIndex = 0,
            explanation = "Separation of concerns allows clean isolation of data models, view state, and UI components.",
            source = QuestionSource.AI_GENERATED
        ),
        Question(
            id = "ai_q_${System.currentTimeMillis()}_2",
            topicId = topicId,
            text = "What is a primary advantage of asynchronous execution when handling data streams in $topicName?",
            options = listOf("Preventing thread starvation and UI stutter", "Freezing the application until completion", "Consuming maximum CPU cycles", "Bypassing operating system security"),
            correctIndex = 0,
            explanation = "Asynchronous processing keeps the main thread responsive, preventing UI freezes and application not responding (ANR) dialogs.",
            source = QuestionSource.AI_GENERATED
        ),
        Question(
            id = "ai_q_${System.currentTimeMillis()}_3",
            topicId = topicId,
            text = "When reviewing best practices for $topicName, how should sensitive configuration data be handled?",
            options = listOf("Stored securely in encrypted local storage or backend environment variables", "Hardcoded directly in public source files", "Printed in plain text logs", "Embedded in UI strings"),
            correctIndex = 0,
            explanation = "Hardcoding secrets in source files poses severe security risks; encrypted local storage or secure environment configuration must be used.",
            source = QuestionSource.AI_GENERATED
        )
    )

    return (1..count).map { idx ->
        val template = samplePool[(idx - 1) % samplePool.size]
        template.copy(
            id = "ai_q_${System.currentTimeMillis()}_$idx",
            text = "${template.text} [AI Generated - $topicName]"
        )
    }
}

@Composable
private fun GeneratedQuestionPreviewList(
    questions: List<Question>,
    savedSuccess: Boolean,
    onSave: () -> Unit,
    onDiscard: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.Spacing8),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "GENERATED QUESTIONS (${questions.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextDim
            )
            if (savedSuccess) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Success, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(Dimens.Spacing4))
                    Text("Saved to local storage", fontSize = 12.sp, color = Success, fontWeight = FontWeight.Bold)
                }
            }
        }

        questions.forEachIndexed { index, q ->
            AppCard(modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.Spacing12)) {
                Text(
                    "Q${index + 1}: ${q.text}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp,
                    modifier = Modifier.padding(bottom = Dimens.Spacing8)
                )
                Column(modifier = Modifier.padding(bottom = Dimens.Spacing8)) {
                    q.options.forEachIndexed { optIdx, opt ->
                        val prefix = ('A' + optIdx)
                        Text("$prefix) $opt", color = TextDim, fontSize = 13.sp, modifier = Modifier.padding(vertical = Dimens.Spacing2))
                    }
                }
                AppCard(backgroundColor = AccentBackground, modifier = Modifier.padding(top = Dimens.Spacing4)) {
                    Text(
                        "Explanation: ${q.explanation}",
                        color = AccentTextOnTint,
                        fontSize = 12.5.sp
                    )
                }
            }
        }

        Row(modifier = Modifier.padding(top = Dimens.Spacing8)) {
            GhostButton(text = "Discard", onClick = onDiscard, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(Dimens.Spacing12))
            PrimaryButton(
                text = if (savedSuccess) "Saved ✓" else "Add to my questions",
                onClick = onSave,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun OfflineBanner() {
    AppCard(backgroundColor = OfflineBannerBackground, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                Icons.Filled.WarningAmber,
                contentDescription = null,
                tint = OfflineBannerText,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(Dimens.Spacing8))
            Text(
                "No connection right now. You can still practice with your saved and previously generated questions offline.",
                color = OfflineBannerText,
                fontSize = 12.5.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AIGenerateScreenPreview() {
    QuizRevisionAppTheme {
        AIGenerateScreen(
            onAboutClick = {},
            onNavBarSelect = {}
        )
    }
}
