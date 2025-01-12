package com.example.financialwunderwaffe.retrofit.database.calculationShareOfAsset.shareOfAsset

import com.example.financialwunderwaffe.retrofit.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CalculationShareOfAssetApiClient {

    val calculationShareOfAssetApiService: CalculationShareOfAssetApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.getURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CalculationShareOfAssetApiService::class.java)
    }

}