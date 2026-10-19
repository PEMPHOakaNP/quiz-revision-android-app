package com.quizrevision.smartquizandrevisionapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizrevision.smartquizandrevisionapp.data.SampleData
import com.quizrevision.smartquizandrevisionapp.data.model.QuizResult
import com.quizrevision.smartquizandrevisionapp.navigation.Screen
import com.quizrevision.smartquizandrevisionapp.ui.components.AppBottomBar
import com.quizrevision.smartquizandrevisionapp.ui.components.AppCard
import com.quizrevision.smartquizandrevisionapp.ui.components.AppTopBar
import com.quizrevision.smartquizandrevisionapp.ui.theme.*

/**
 * Progress screen showing performance charts, quiz statistics, and recent results.
 */
@Composable
fun ProgressScreen(
    onAboutClick: () -> Unit,
    onNavBarSelect: (String) -> Unit
) {
    Scaffold(
        containerColor = AppBackground,
        topBar = {
            AppTopBar(
                title = "Progress",
                showAbout = true,
                onAboutClick = onAboutClick
            )
        },
        bottomBar = { AppBottomBar(currentRoute = Screen.Progress.route, onSelect = onNavBarSelect) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            Column(modifier = Modifier.padding(horizontal = Dimens.Spacing24).padding(top = Dimens.Spacing20)) {
                Text("Your progress", style = MaterialTheme.typography.headlineSmall, fontSize = 22.sp, modifier = Modifier.padding(bottom = Dimens.Spacing16))

                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "LAST 6 QUIZZES",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextDim,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = Dimens.Spacing12)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().height(90.dp),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing8)
                    ) {
                        SampleData.weeklyScores.forEachIndexed { index, value ->
                            Column(
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(fraction = value / 100f)
                                        .clip(RoundedCornerShape(topStart = Dimens.CornerRadius12, topEnd = Dimens.CornerRadius12))
                                        .background(Primary)
                                )
                                Spacer(Modifier.height(Dimens.Spacing4))
                                Text(SampleData.weeklyLabels[index], fontSize = 10.sp, color = TextDim)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(Dimens.Spacing16))

                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12), modifier = Modifier.padding(bottom = Dimens.Spacing16)) {
                    AppCard(modifier = Modifier.weight(1f)) {
                        Text("${SampleData.quizzesCompletedCount()}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                        Text("Quizzes taken", fontSize = 11.5.sp, color = TextDim)
                    }
                    AppCard(modifier = Modifier.weight(1f)) {
                        Text("${SampleData.averageScorePercent()}%", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Primary)
                        Text("Average score", fontSize = 11.5.sp, color = TextDim)
                    }
                }

                Text(
                    "RECENT RESULTS",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDim,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = Dimens.Spacing8)
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = Dimens.Spacing24, vertical = Dimens.Spacing4),
                verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
            ) {
                items(SampleData.recentResults) { result -> ResultRow(result) }
            }
        }
    }
}

@Composable
private fun ResultRow(result: QuizResult) {
    val percent = (result.score * 100) / result.total
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(PrimaryTint),
                contentAlignment = Alignment.Center
            ) {
                Text("$percent%", fontWeight = FontWeight.ExtraBold, fontSize = 12.5.sp, color = Primary)
            }
            Spacer(Modifier.width(Dimens.Spacing12))
            Column(modifier = Modifier.weight(1f)) {
                Text(result.topicName, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                Text(result.date, fontSize = 12.sp, color = TextDim)
            }
            Text("${result.score}/${result.total}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressScreenPreview() {
    QuizRevisionAppTheme {
        ProgressScreen(
            onAboutClick = {},
            onNavBarSelect = {}
        )
    }
}
