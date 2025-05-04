package com.example.financialwunderwaffe.retrofit.database.analytics

import com.example.financialwunderwaffe.retrofit.database.analytics.model.BudgetAnalytics
import com.example.financialwunderwaffe.retrofit.database.analytics.model.CapitalAnalytics
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.UUID

interface AnalyticsApiService {

    @GET("analytics/budget")
    fun getBudgetByMonth(
        @Header("Authorization") token: String,
        @Query("userUID") userUID: UUID
    ): Call<List<BudgetAnalytics>>

    @GET("analytics/capital")
    fun getCapitalByMonth(
        @Header("Authorization") token: String,
        @Query("userUID") userUID: UUID
    ): Call<List<CapitalAnalytics>>

}