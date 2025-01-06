package com.example.financialwunderwaffe.retrofit.database.user

import com.example.financialwunderwaffe.retrofit.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserApiClient {

    val userAPIService: UserApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.getURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

}