package com.example.financialwunderwaffe.retrofit.database.calculationShareOfAsset.typeAsset

import com.example.financialwunderwaffe.retrofit.BaseURL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TypeAssetApiClient {

    val typeAssetApiService: TypeAssetApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BaseURL.getURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TypeAssetApiService::class.java)
    }

}