package com.quizrevision.smartquizandrevisionapp.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizrevision.smartquizandrevisionapp.R
import com.quizrevision.smartquizandrevisionapp.ui.components.AppCard
import com.quizrevision.smartquizandrevisionapp.ui.components.AppTopBar
import com.quizrevision.smartquizandrevisionapp.ui.theme.*

/**
 * About screen displaying application info, features, developer details, and version.
 */
@Composable
fun AboutScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val versionName = getAppVersionName(context)

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            AppTopBar(
                title = stringResource(R.string.about_title),
                showBack = true,
                showAbout = false,
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.Spacing24, vertical = Dimens.Spacing16),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo Mark (72dp) & App Name & Version
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(Dimens.CornerRadius18))
                    .background(Primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = AppSurface,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(Modifier.height(Dimens.Spacing16))

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Version $versionName",
                style = MaterialTheme.typography.bodySmall,
                color = TextDim,
                modifier = Modifier.padding(top = Dimens.Spacing4, bottom = Dimens.Spacing24)
            )

            // What this app does
            AppCard(modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.Spacing16)) {
                Text(
                    text = "WHAT THIS APP DOES",
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = Dimens.Spacing8)
                )
                Text(
                    text = stringResource(R.string.about_what_it_does),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )
            }

            // Key Features
            AppCard(modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.Spacing16)) {
                Text(
                    text = stringResource(R.string.about_key_features_title).uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = Dimens.Spacing8)
                )
                Text(
                    text = stringResource(R.string.about_key_features_list),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    lineHeight = 22.sp
                )
            }

            // About AI Questions
            AppCard(modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.Spacing16)) {
                Text(
                    text = stringResource(R.string.about_ai_title).uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = Dimens.Spacing8)
                )
                Text(
                    text = stringResource(R.string.about_ai_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )
            }

            // Built With
            AppCard(modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.Spacing16)) {
                Text(
                    text = "BUILT WITH",
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = Dimens.Spacing8)
                )
                Text(
                    text = stringResource(R.string.about_built_with),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )
            }

            // Developer Card
            AppCard(modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.Spacing24)) {
                Text(
                    text = stringResource(R.string.about_developer_title).uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = Dimens.Spacing8)
                )
                Text(
                    text = stringResource(R.string.about_developer_name),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(Dimens.Spacing4))
                Text(
                    text = stringResource(R.string.about_developer_course),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDim
                )
                Spacer(Modifier.height(Dimens.Spacing4))
                Text(
                    text = stringResource(R.string.about_developer_institution),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDim
                )
                Spacer(Modifier.height(Dimens.Spacing4))
                Text(
                    text = stringResource(R.string.about_developer_year),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDim
                )
            }
        }
    }
}

private fun getAppVersionName(context: Context): String {
    return try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        pInfo.versionName ?: "1.0"
    } catch (e: Exception) {
        "1.0"
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    QuizRevisionAppTheme {
        AboutScreen(onBack = {})
    }
}
