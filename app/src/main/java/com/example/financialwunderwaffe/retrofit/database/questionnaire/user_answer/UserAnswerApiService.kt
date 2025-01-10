package com.example.financialwunderwaffe.retrofit.database.questionnaire.user_answer

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.util.UUID

interface UserAnswerApiService {

    @GET("user_answers")
    fun getUserAnswerByUID(
        @Header("Authorization") token: String,
        @Query("userUID") userUID: UUID
    ): Call<List<UserAnswer>>

    @POST("user_answers")
    fun createUserAnswer(
        @Header("Authorization") token: String,
        @Body listUserAnswer: List<UserAnswer>
    ): Call<List<Long>>

    @PUT("user_answers")
    fun updateUserAnswer(
        @Header("Authorization") token: String,
        @Body listUserAnswer: List<UserAnswer>
    ): Call<Boolean>

}