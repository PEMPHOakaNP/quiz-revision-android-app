package com.quizrevision.smartquizandrevisionapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizrevision.smartquizandrevisionapp.navigation.Screen
import com.quizrevision.smartquizandrevisionapp.ui.theme.*
import java.util.Calendar

/** The four-tab bottom bar shown on Home, Course Selection, Progress, and AI. */
@Composable
fun AppBottomBar(currentRoute: String, onSelect: (String) -> Unit) {
    NavigationBar(containerColor = AppSurface, tonalElevation = 0.dp) {
        val items = listOf(
            Triple(Screen.Home.route, "Home", Icons.Filled.Home),
            Triple(Screen.CourseSelection.route, "Courses", Icons.AutoMirrored.Filled.MenuBook),
            Triple(Screen.Progress.route, "Progress", Icons.Filled.BarChart),
            Triple(Screen.AIGenerate.route, "AI", Icons.Filled.AutoAwesome)
        )
        items.forEach { (route, label, icon) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = { onSelect(route) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = TextDim,
                    unselectedTextColor = TextDim,
                    indicatorColor = AppSurface
                )
            )
        }
    }
}

/** Unified App Top Bar supporting primary, secondary, home, and quiz variants with 48dp min touch targets. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String = "Quiz & Revision",
    subtitle: String? = null,
    showBack: Boolean = false,
    showClose: Boolean = false,
    showAbout: Boolean = true,
    isDarkHeader: Boolean = false,
    onBack: () -> Unit = {},
    onClose: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    val containerColor = if (isDarkHeader) PrimaryDark else AppBackground
    val contentColor = if (isDarkHeader) AppSurface else TextPrimary

    TopAppBar(
        title = {
            if (isDarkHeader || (!showBack && !showClose)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = Dimens.Spacing8)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isDarkHeader) AppSurface.copy(alpha = 0.18f) else Primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MenuBook,
                            contentDescription = null,
                            tint = AppSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(Dimens.Spacing12))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = contentColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = contentColor,
                        fontWeight = FontWeight.Bold
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDarkHeader) contentColor.copy(alpha = 0.75f) else TextDim
                        )
                    }
                }
            }
        },
        navigationIcon = {
            if (showBack) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(Dimens.TouchTargetMin)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = contentColor
                    )
                }
            } else if (showClose) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(Dimens.TouchTargetMin)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = contentColor
                    )
                }
            }
        },
        actions = {
            if (showAbout) {
                IconButton(
                    onClick = onAboutClick,
                    modifier = Modifier.size(Dimens.TouchTargetMin)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "About",
                        tint = contentColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor,
            navigationIconContentColor = contentColor
        ),
        windowInsets = TopAppBarDefaults.windowInsets
    )
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(Dimens.CornerRadius12),
        colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = AppSurface)
    ) {
        leadingIcon?.invoke()
        if (leadingIcon != null) Spacer(Modifier.width(Dimens.Spacing8))
        Text(text, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}

@Composable
fun GhostButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(Dimens.CornerRadius12),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary),
        border = BorderStroke(1.5.dp, Primary)
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}

/** A progress bar matching the thin rounded track used throughout the design. */
@Composable
fun ThinProgressBar(percent: Int, modifier: Modifier = Modifier, trackColor: Color = ProgressTrack, fillColor: Color = Primary) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(7.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = (percent.coerceIn(0, 100)) / 100f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(5.dp))
                .background(fillColor)
        )
    }
}

/** The basic card shell (white surface, soft border, rounded corners) used everywhere. */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppSurface,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(Dimens.CornerRadius18))
            .background(backgroundColor)
            .then(if (backgroundColor == AppSurface) Modifier.border(1.dp, Border, RoundedCornerShape(Dimens.CornerRadius18)) else Modifier)
            .padding(Dimens.Spacing16)
    ) {
        content()
    }
}

data class GreetingInfo(
    val greeting: String,
    val sessionLabel: String,
    val icon: ImageVector,
    val accentColor: Color
)

fun getTimeBasedGreetingInfo(): GreetingInfo {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> GreetingInfo("Good morning", "Morning Session", Icons.Filled.WbSunny, GreetingMorning)
        in 12..16 -> GreetingInfo("Good afternoon", "Afternoon Practice", Icons.Filled.WbSunny, GreetingAfternoon)
        in 17..20 -> GreetingInfo("Good evening", "Evening Revision", Icons.Filled.NightsStay, GreetingEvening)
        else -> GreetingInfo("Good night", "Late Night Study", Icons.Filled.Bedtime, GreetingNight)
    }
}

/** Greeting card at the beginning of the home body. */
@Composable
fun AppHeaderWithFloatingGreeting() {
    val greetingInfo = remember { getTimeBasedGreetingInfo() }

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Spacing24)
            .padding(top = Dimens.Spacing16, bottom = Dimens.Spacing12)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Welcome back, Student",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontSize = 22.sp
                )
                Text(
                    text = "Ready to revise?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextDim,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = Dimens.Spacing2)
                )
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(greetingInfo.accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = greetingInfo.icon,
                    contentDescription = null,
                    tint = greetingInfo.accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
