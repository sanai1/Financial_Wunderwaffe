package com.example.financialwunderwaffe.retrofit.database.questionnaire.model

data class Question(
    val id: Long,
    val text: String,
    val isEnabled: Boolean,
    val listAnswers: List<Answer>
)