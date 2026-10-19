package com.quizrevision.smartquizandrevisionapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizrevision.smartquizandrevisionapp.data.SampleData
import com.quizrevision.smartquizandrevisionapp.data.model.QuizResult
import com.quizrevision.smartquizandrevisionapp.ui.components.AppCard
import com.quizrevision.smartquizandrevisionapp.ui.components.GhostButton
import com.quizrevision.smartquizandrevisionapp.ui.components.PrimaryButton
import com.quizrevision.smartquizandrevisionapp.ui.theme.*

/**
 * Result screen showing score summary, percentage, and answer review dialog.
 */
@Composable
fun ResultScreen(
    topicId: String,
    score: Int,
    total: Int,
    onBackToHome: () -> Unit
) {
    val topicName = SampleData.topicName(topicId)
    val courseName = SampleData.courseNameForTopic(topicId)
    val percent = if (total > 0) (score * 100) / total else 0
    val questions = remember(topicId) { SampleData.questionsFor(topicId) }
    var showReviewDialog by remember { mutableStateOf(false) }

    LaunchedEffect(topicId, score, total) {
        SampleData.addQuizResult(
            QuizResult(
                id = "r_${System.currentTimeMillis()}",
                courseName = courseName,
                topicName = topicName,
                score = score,
                total = total,
                date = "Just now"
            )
        )
    }

    Scaffold(containerColor = AppBackground) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Dimens.Spacing24)
                    .padding(top = Dimens.Spacing32, bottom = Dimens.Spacing16),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Quiz complete", color = TextDim, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = Dimens.Spacing16))

                Box(
                    modifier = Modifier
                        .size(148.dp)
                        .clip(CircleShape)
                        .background(ProgressTrack),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(118.dp)
                            .clip(CircleShape)
                            .background(AppSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$score/$total", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)
                            Text("$percent% correct", color = TextDim, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(Modifier.height(Dimens.Spacing20))

                Text(
                    if (percent >= 70) "Nice work!" else "Good effort — keep practicing",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "$topicName • $courseName",
                    color = TextDim,
                    fontSize = 13.5.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = Dimens.Spacing24)
                )

                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Quiz Performance Summary",
                        fontWeight = FontWeight.Bold,
                        color = Primary,
                        fontSize = 13.5.sp,
                        modifier = Modifier.padding(bottom = Dimens.Spacing4)
                    )
                    Text(
                        "You answered $score out of $total questions correctly. Tap 'Review all answers' below to inspect each question and explanation in detail.",
                        color = TextDim,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }

                Spacer(Modifier.height(Dimens.Spacing16))

                AppCard(backgroundColor = SuccessBackground, modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Check, contentDescription = null, tint = Success, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(Dimens.Spacing12))
                        Text(
                            "$score of $total answered correctly on this topic.",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = Dimens.Spacing24).padding(bottom = Dimens.Spacing24)) {
                PrimaryButton(text = "Review all answers", onClick = { showReviewDialog = true }, modifier = Modifier.padding(bottom = Dimens.Spacing12))
                GhostButton(text = "Back to home", onClick = onBackToHome)
            }
        }
    }

    if (showReviewDialog) {
        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            containerColor = AppSurface,
            title = { Text("Answer Review", fontWeight = FontWeight.Bold, color = TextPrimary) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
                ) {
                    questions.forEachIndexed { idx, q ->
                        AppCard(modifier = Modifier.fillMaxWidth()) {
                            Text("Q${idx + 1}: ${q.text}", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, modifier = Modifier.padding(bottom = Dimens.Spacing4))
                            Text("Correct Answer: ${q.options[q.correctIndex]}", color = Success, fontWeight = FontWeight.SemiBold, fontSize = 12.5.sp)
                            Spacer(Modifier.height(Dimens.Spacing4))
                            Text(q.explanation, color = TextDim, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showReviewDialog = false }) {
                    Text("Close", fontWeight = FontWeight.Bold, color = Primary)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    QuizRevisionAppTheme {
        ResultScreen(
            topicId = "t1_1",
            score = 8,
            total = 10,
            onBackToHome = {}
        )
    }
}
