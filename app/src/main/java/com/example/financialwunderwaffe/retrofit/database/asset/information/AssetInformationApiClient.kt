package com.example.financialwunderwaffe.retrofit.database.asset.information

import com.example.financialwunderwaffe.retrofit.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AssetInformationApiClient {

    val assetInformationAPIService: AssetInformationApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.getURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AssetInformationApiService::class.java)
    }

}