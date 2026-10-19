package com.quizrevision.smartquizandrevisionapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizrevision.smartquizandrevisionapp.ui.theme.Dimens
import com.quizrevision.smartquizandrevisionapp.ui.theme.Primary
import com.quizrevision.smartquizandrevisionapp.ui.theme.QuizRevisionAppTheme
import kotlinx.coroutines.delay

/**
 * The landing / splash screen shown before Home loads.
 */
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(Dimens.CornerRadius28))
                .background(Color.White.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(Modifier.height(Dimens.Spacing24))

        Text(
            "Quiz & Revision",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 26.sp
        )
        Text(
            "Practice. Review. Improve.",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 13.5.sp,
            modifier = Modifier.padding(top = Dimens.Spacing4, bottom = 60.dp)
        )

        Row {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = Dimens.Spacing4)
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = if (index == 1) 1f else 0.6f))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    QuizRevisionAppTheme {
        SplashScreen(onFinished = {})
    }
}
