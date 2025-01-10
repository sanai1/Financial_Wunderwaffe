package com.example.financialwunderwaffe.retrofit.database.questionnaire.user_answer

import com.example.financialwunderwaffe.retrofit.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserAnswerApiClient {

    val userAnswerAPIService: UserAnswerApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.getURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAnswerApiService::class.java)
    }

}