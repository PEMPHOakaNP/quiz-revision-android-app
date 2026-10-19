package com.quizrevision.smartquizandrevisionapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizrevision.smartquizandrevisionapp.data.SampleData
import com.quizrevision.smartquizandrevisionapp.data.model.Topic
import com.quizrevision.smartquizandrevisionapp.ui.components.AppCard
import com.quizrevision.smartquizandrevisionapp.ui.components.AppTopBar
import com.quizrevision.smartquizandrevisionapp.ui.theme.*

@Composable
fun TopicSelectionScreen(
    courseId: String,
    onBack: () -> Unit,
    onAboutClick: () -> Unit,
    onTopicSelected: (String) -> Unit
) {
    val courseName = SampleData.courseName(courseId)
    val topics = SampleData.topicsFor(courseId)

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            AppTopBar(
                title = courseName.ifBlank { "Course" },
                subtitle = "${topics.size} topics",
                showBack = true,
                onAboutClick = onAboutClick,
                onBack = onBack
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = Dimens.Spacing24, vertical = Dimens.Spacing16),
                verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
            ) {
                items(topics) { topic ->
                    TopicRow(topic = topic, onClick = { onTopicSelected(topic.id) })
                }
            }
        }
    }
}

@Composable
private fun TopicRow(topic: Topic, onClick: () -> Unit) {
    AppCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(Dimens.CornerRadius12))
                    .background(PrimaryTint),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(Dimens.Spacing12))
            Column(modifier = Modifier.weight(1f)) {
                Text(topic.name, style = MaterialTheme.typography.titleMedium, fontSize = 14.5.sp)
                Text(
                    "${topic.questionCount} questions • ${topic.attempted} attempted",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDim
                )
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = TextDim)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopicSelectionScreenPreview() {
    QuizRevisionAppTheme {
        TopicSelectionScreen(
            courseId = "c1",
            onBack = {},
            onAboutClick = {},
            onTopicSelected = {}
        )
    }
}
