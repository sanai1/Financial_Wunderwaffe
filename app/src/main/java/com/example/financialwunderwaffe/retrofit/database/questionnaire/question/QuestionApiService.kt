package com.example.financialwunderwaffe.retrofit.database.questionnaire.question

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface QuestionApiService {

    @GET("question")
    fun getQuestions(
        @Header("Authorization") token: String
    ): Call<List<Question>>

}