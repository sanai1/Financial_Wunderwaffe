package com.example.financialwunderwaffe.retrofit.database.analytics

import com.example.financialwunderwaffe.retrofit.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AnalyticsApiClient {

    val analyticsAPIService: AnalyticsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.getURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnalyticsApiService::class.java)
    }

}