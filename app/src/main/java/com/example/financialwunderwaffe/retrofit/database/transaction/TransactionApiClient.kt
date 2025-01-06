package com.example.financialwunderwaffe.retrofit.database.transaction

import com.example.financialwunderwaffe.retrofit.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TransactionApiClient {

    val transactionAPIService: TransactionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.getURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TransactionApiService::class.java)
    }

}