package com.quizrevision.smartquizandrevisionapp.data.model

/**
 * These models mirror the "Data Requirements" section of the Project Idea
 * Documentation: Course Data, Topic Data, Question Data, AI-Generated Question
 * Data, and Quiz Result Data. In the full app, instances of these would be
 * persisted with Room (see the commented Room dependencies in build.gradle.kts)
 * instead of living only in SampleData.kt.
 */

data class Course(
    val id: String,
    val name: String,
    val initials: String,
    val topicCount: Int,
    val questionCount: Int,
)

data class Topic(
    val id: String,
    val courseId: String,
    val name: String,
    val questionCount: Int,
    val attempted: Int
)

enum class QuestionSource { LOCAL, AI_GENERATED }

data class Question(
    val id: String,
    val topicId: String,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val source: QuestionSource = QuestionSource.LOCAL
)

data class QuizResult(
    val id: String,
    val courseName: String,
    val topicName: String,
    val score: Int,
    val total: Int,
    val date: String
)
