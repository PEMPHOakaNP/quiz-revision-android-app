package com.quizrevision.smartquizandrevisionapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import com.quizrevision.smartquizandrevisionapp.data.model.Course
import com.quizrevision.smartquizandrevisionapp.navigation.Screen
import com.quizrevision.smartquizandrevisionapp.ui.components.AppBottomBar
import com.quizrevision.smartquizandrevisionapp.ui.components.AppCard
import com.quizrevision.smartquizandrevisionapp.ui.components.AppTopBar
import com.quizrevision.smartquizandrevisionapp.ui.theme.*

@Composable
fun CourseSelectionScreen(
    onCourseSelected: (String) -> Unit,
    onAboutClick: () -> Unit,
    onNavBarSelect: (String) -> Unit
) {
    Scaffold(
        containerColor = AppBackground,
        topBar = {
            AppTopBar(
                title = "Courses",
                showAbout = true,
                onAboutClick = onAboutClick
            )
        },
        bottomBar = { AppBottomBar(currentRoute = Screen.CourseSelection.route, onSelect = onNavBarSelect) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(modifier = Modifier.padding(horizontal = Dimens.Spacing24).padding(top = Dimens.Spacing20, bottom = Dimens.Spacing12)) {
                Text("Your courses", style = MaterialTheme.typography.headlineSmall, fontSize = 22.sp, color = TextPrimary)
                Text("Pick a course to see its revision topics", style = MaterialTheme.typography.bodyMedium, color = TextDim)
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = Dimens.Spacing24, vertical = Dimens.Spacing4),
                verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
            ) {
                items(SampleData.courses) { course ->
                    CourseRow(course = course, onClick = { onCourseSelected(course.id) })
                }
            }
        }
    }
}

@Composable
private fun CourseRow(course: Course, onClick: () -> Unit) {
    AppCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(Dimens.CornerRadius14))
                    .background(PrimaryTint),
                contentAlignment = Alignment.Center
            ) {
                Text(course.initials, fontWeight = FontWeight.ExtraBold, color = Primary)
            }
            Spacer(Modifier.width(Dimens.Spacing12))
            Column(modifier = Modifier.weight(1f)) {
                Text(course.name, style = MaterialTheme.typography.titleMedium, fontSize = 15.sp)
                Text(
                    "${course.topicCount} topics • ${course.questionCount} questions",
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
fun CourseSelectionScreenPreview() {
    QuizRevisionAppTheme {
        CourseSelectionScreen(
            onCourseSelected = {},
            onAboutClick = {},
            onNavBarSelect = {}
        )
    }
}
