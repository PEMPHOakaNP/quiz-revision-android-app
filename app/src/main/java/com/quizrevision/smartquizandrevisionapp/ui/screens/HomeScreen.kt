package com.quizrevision.smartquizandrevisionapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizrevision.smartquizandrevisionapp.data.SampleData
import com.quizrevision.smartquizandrevisionapp.navigation.Screen
import com.quizrevision.smartquizandrevisionapp.ui.components.*
import com.quizrevision.smartquizandrevisionapp.ui.theme.*

@Composable
fun HomeScreen(
    onBrowseCourses: () -> Unit,
    onResumeQuiz: () -> Unit,
    onAboutClick: () -> Unit,
    onNavBarSelect: (String) -> Unit
) {
    Scaffold(
        containerColor = AppBackground,
        topBar = {
            AppTopBar(
                title = "Quiz & Revision",
                isDarkHeader = true,
                showAbout = true,
                onAboutClick = onAboutClick
            )
        },
        bottomBar = { AppBottomBar(currentRoute = Screen.Home.route, onSelect = onNavBarSelect) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = Dimens.Spacing16)
        ) {
            // Greeting Card at the beginning of the home body
            AppHeaderWithFloatingGreeting()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Spacing24)
            ) {
                // Available Quizzes Card
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "AVAILABLE QUIZZES",
                        style = MaterialTheme.typography.bodySmall,
                        color = Primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Explore Courses & Practice Quizzes",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = Dimens.Spacing4)
                    )
                    Text(
                        "Browse university subjects, select topics, and test your knowledge with interactive quizzes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDim,
                        modifier = Modifier.padding(top = Dimens.Spacing4, bottom = Dimens.Spacing12)
                    )
                    GhostButton(text = "View All Courses", onClick = onBrowseCourses)
                }

                Spacer(Modifier.height(Dimens.Spacing16))

                // Revision Card / Section
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "REVISION",
                        style = MaterialTheme.typography.bodySmall,
                        color = Primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Mobile Applications Development",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = Dimens.Spacing4)
                    )
                    Text(
                        "Threading & Services",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDim,
                        modifier = Modifier.padding(bottom = Dimens.Spacing12)
                    )
                    ThinProgressBar(percent = 65, modifier = Modifier.padding(bottom = Dimens.Spacing12))
                    PrimaryButton(text = "Revise Now", onClick = onResumeQuiz)
                }

                Spacer(Modifier.height(Dimens.Spacing16))

                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)) {
                    AppCard(modifier = Modifier.weight(1f)) {
                        Text("${SampleData.quizzesCompletedCount()}", style = MaterialTheme.typography.headlineSmall, fontSize = 22.sp)
                        Text("Quizzes completed", style = MaterialTheme.typography.bodySmall, color = TextDim)
                    }
                    AppCard(modifier = Modifier.weight(1f)) {
                        Text("${SampleData.averageScorePercent()}%", style = MaterialTheme.typography.headlineSmall, fontSize = 22.sp, color = Primary)
                        Text("Average score", style = MaterialTheme.typography.bodySmall, color = TextDim)
                    }
                }

                Spacer(Modifier.height(Dimens.Spacing20))

                Text("Quick actions", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = Dimens.Spacing8))

                PrimaryButton(
                    text = "Browse courses & topics",
                    onClick = onBrowseCourses,
                    modifier = Modifier.padding(bottom = Dimens.Spacing16),
                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )

                AppCard(backgroundColor = AccentBackground) {
                    Text(
                        buildString {
                            append("Offline ready. ")
                            append("Saved questions, results and progress stay available without internet. AI generation needs a connection.")
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = AccentTextOnTint,
                        fontSize = 12.5.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    QuizRevisionAppTheme {
        HomeScreen(
            onBrowseCourses = {},
            onResumeQuiz = {},
            onAboutClick = {},
            onNavBarSelect = {}
        )
    }
}
