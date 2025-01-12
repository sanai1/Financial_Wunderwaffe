package com.example.financialwunderwaffe.retrofit.database.calculationShareOfAsset.typeAsset

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface TypeAssetApiService {

    @GET("") // TODO: написать путь
    fun getAllTypeAsset(
        @Header("Authorization") token: String
    ): Call<TypeAsset>

}