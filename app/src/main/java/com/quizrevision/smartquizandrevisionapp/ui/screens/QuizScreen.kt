package com.quizrevision.smartquizandrevisionapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizrevision.smartquizandrevisionapp.data.SampleData
import com.quizrevision.smartquizandrevisionapp.ui.components.AppCard
import com.quizrevision.smartquizandrevisionapp.ui.components.AppTopBar
import com.quizrevision.smartquizandrevisionapp.ui.components.PrimaryButton
import com.quizrevision.smartquizandrevisionapp.ui.components.ThinProgressBar
import com.quizrevision.smartquizandrevisionapp.ui.theme.*

/**
 * Quiz screen implementing question flow, answer submission, and automatic marking.
 */
@Composable
fun QuizScreen(
    topicId: String,
    onExit: () -> Unit,
    onAboutClick: () -> Unit,
    onQuizComplete: (score: Int, total: Int) -> Unit
) {
    val questions = remember(topicId) { SampleData.questionsFor(topicId) }
    val topicName = remember(topicId) { SampleData.topicName(topicId) }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var correctCount by remember { mutableIntStateOf(0) }
    var hasSubmittedCurrent by remember { mutableStateOf(false) }

    if (questions.isEmpty()) {
        Scaffold(
            containerColor = AppBackground,
            topBar = {
                AppTopBar(
                    title = topicName,
                    showClose = true,
                    onClose = onExit,
                    onAboutClick = onAboutClick
                )
            }
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No questions available for this topic yet.", color = TextDim)
            }
        }
        return
    }

    val question = questions[currentIndex]
    val completedCount = currentIndex + (if (hasSubmittedCurrent) 1 else 0)
    val progressPercent = ((completedCount * 100) / questions.size)

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            Column {
                AppTopBar(
                    title = "Question ${currentIndex + 1} of ${questions.size}",
                    showClose = true,
                    onClose = onExit,
                    onAboutClick = onAboutClick
                )
                ThinProgressBar(percent = progressPercent)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = Dimens.Spacing24, vertical = Dimens.Spacing16)
        ) {
            Text(
                topicName,
                style = MaterialTheme.typography.bodySmall,
                color = Primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = Dimens.Spacing8)
            )

            AppCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    question.text,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 17.sp,
                    lineHeight = 24.sp
                )
            }

            Spacer(Modifier.height(Dimens.Spacing20))

            question.options.forEachIndexed { index, optionText ->
                val isSelected = selectedOption == index
                val isCorrectAnswer = index == question.correctIndex

                val borderColor = when {
                    !hasSubmittedCurrent && isSelected -> Primary
                    hasSubmittedCurrent && isCorrectAnswer -> Success
                    hasSubmittedCurrent && isSelected && !isCorrectAnswer -> ErrorColor
                    else -> Border
                }
                val fillColor = when {
                    !hasSubmittedCurrent && isSelected -> PrimaryTint
                    hasSubmittedCurrent && isCorrectAnswer -> SuccessBackground
                    hasSubmittedCurrent && isSelected && !isCorrectAnswer -> ErrorColor.copy(alpha = 0.08f)
                    else -> AppSurface
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimens.Spacing12)
                        .clip(RoundedCornerShape(Dimens.CornerRadius14))
                        .background(fillColor)
                        .border(1.5.dp, borderColor, RoundedCornerShape(Dimens.CornerRadius14))
                        .clickable(enabled = !hasSubmittedCurrent) { selectedOption = index }
                        .padding(Dimens.Spacing16)
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (isSelected || (hasSubmittedCurrent && isCorrectAnswer)) borderColor else AppSurface)
                    )
                    Spacer(Modifier.width(Dimens.Spacing12))
                    Text(optionText, fontWeight = FontWeight.SemiBold, fontSize = 14.5.sp, color = TextPrimary)
                }
            }

            if (hasSubmittedCurrent) {
                AppCard(backgroundColor = AccentBackground, modifier = Modifier.padding(top = Dimens.Spacing4)) {
                    Text(
                        question.explanation,
                        style = MaterialTheme.typography.bodySmall,
                        color = AccentTextOnTint,
                        fontSize = 12.5.sp
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            PrimaryButton(
                text = if (!hasSubmittedCurrent) "Submit answer"
                else if (currentIndex == questions.lastIndex) "See results"
                else "Next question",
                enabled = hasSubmittedCurrent || selectedOption != null,
                onClick = {
                    if (!hasSubmittedCurrent) {
                        if (selectedOption != null) {
                            hasSubmittedCurrent = true
                            if (selectedOption == question.correctIndex) correctCount++
                        }
                    } else {
                        if (currentIndex == questions.lastIndex) {
                            onQuizComplete(correctCount, questions.size)
                        } else {
                            currentIndex++
                            selectedOption = null
                            hasSubmittedCurrent = false
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    QuizRevisionAppTheme {
        QuizScreen(
            topicId = "t1_1",
            onExit = {},
            onAboutClick = {},
            onQuizComplete = { _, _ -> }
        )
    }
}
