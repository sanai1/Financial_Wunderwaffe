package com.example.financialwunderwaffe.retrofit.database.questionnaire.question

import com.example.financialwunderwaffe.retrofit.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object QuestionApiClient {

    val questionAPIService: QuestionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.getURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionApiService::class.java)
    }

}