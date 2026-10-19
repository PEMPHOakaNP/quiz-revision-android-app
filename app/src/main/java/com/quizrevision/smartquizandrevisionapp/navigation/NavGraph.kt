package com.quizrevision.smartquizandrevisionapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quizrevision.smartquizandrevisionapp.ui.screens.AboutScreen
import com.quizrevision.smartquizandrevisionapp.ui.screens.AIGenerateScreen
import com.quizrevision.smartquizandrevisionapp.ui.screens.CourseSelectionScreen
import com.quizrevision.smartquizandrevisionapp.ui.screens.HomeScreen
import com.quizrevision.smartquizandrevisionapp.ui.screens.ProgressScreen
import com.quizrevision.smartquizandrevisionapp.ui.screens.QuizScreen
import com.quizrevision.smartquizandrevisionapp.ui.screens.ResultScreen
import com.quizrevision.smartquizandrevisionapp.ui.screens.SplashScreen
import com.quizrevision.smartquizandrevisionapp.ui.screens.TopicSelectionScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Screen.Home.route) {
                        // Remove Splash from the back stack so the back button
                        // never returns the user to it.
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onBrowseCourses = { navController.navigate(Screen.CourseSelection.route) },
                onResumeQuiz = { navController.navigate(Screen.Quiz.createRoute("t2_3")) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onNavBarSelect = { route -> navController.navigateFromBottomBar(route) }
            )
        }

        composable(Screen.CourseSelection.route) {
            CourseSelectionScreen(
                onCourseSelected = { courseId ->
                    navController.navigate(Screen.TopicSelection.createRoute(courseId))
                },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onNavBarSelect = { route -> navController.navigateFromBottomBar(route) }
            )
        }

        composable(
            route = Screen.TopicSelection.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            TopicSelectionScreen(
                courseId = courseId,
                onBack = { navController.popBackStack() },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onTopicSelected = { topicId ->
                    navController.navigate(Screen.Quiz.createRoute(topicId))
                }
            )
        }

        composable(
            route = Screen.Quiz.route,
            arguments = listOf(navArgument("topicId") { type = NavType.StringType })
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId") ?: ""
            QuizScreen(
                topicId = topicId,
                onExit = { navController.popBackStack() },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onQuizComplete = { score, total ->
                    navController.navigate(Screen.Result.createRoute(topicId, score, total)) {
                        // Finishing a quiz replaces it on the back stack, so
                        // pressing back from Results goes to Home, not to the
                        // last question.
                        popUpTo(Screen.Quiz.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("topicId") { type = NavType.StringType },
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId") ?: ""
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0
            ResultScreen(
                topicId = topicId,
                score = score,
                total = total,
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Progress.route) {
            ProgressScreen(
                onAboutClick = { navController.navigate(Screen.About.route) },
                onNavBarSelect = { route -> navController.navigateFromBottomBar(route) }
            )
        }

        composable(Screen.AIGenerate.route) {
            AIGenerateScreen(
                onAboutClick = { navController.navigate(Screen.About.route) },
                onNavBarSelect = { route -> navController.navigateFromBottomBar(route) }
            )
        }

        composable(Screen.About.route) {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Shared bottom-nav behaviour for Home / Course Selection / Progress / AI: switch
 * tabs without piling up duplicate screens on the back stack.
 */
private fun NavHostController.navigateFromBottomBar(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
