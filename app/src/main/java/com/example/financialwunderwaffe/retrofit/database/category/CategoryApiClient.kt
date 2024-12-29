package com.example.financialwunderwaffe.retrofit.database.category

import com.example.financialwunderwaffe.retrofit.database.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CategoryApiClient {

    val categoryAPIService: CategoryApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.getURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoryApiService::class.java)
    }

}