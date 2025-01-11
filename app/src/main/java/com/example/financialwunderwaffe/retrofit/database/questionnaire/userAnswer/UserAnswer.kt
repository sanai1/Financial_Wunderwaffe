package com.example.financialwunderwaffe.retrofit.database.questionnaire.userAnswer

import java.util.UUID

data class UserAnswer(
    val id: Long,
    val userUID: UUID,
    val questionID: Long,
    var answerID: Long,
    val date: String
)
