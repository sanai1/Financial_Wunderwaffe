package com.example.financialwunderwaffe.retrofit.database.asset

import com.example.financialwunderwaffe.retrofit.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AssetApiClient {

    val assetAPIService: AssetApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.getURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AssetApiService::class.java)
    }
}