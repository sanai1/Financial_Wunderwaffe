package com.example.financialwunderwaffe.retrofit.database.questionnaire.user_answer

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserAnswerApiService {

    @POST("user_answers")
    fun createUserAnswer(
        @Header("Authorization") token: String,
        @Body listUserAnswer: List<UserAnswer>
    ): Call<List<Long>>

}