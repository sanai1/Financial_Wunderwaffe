package com.example.financialwunderwaffe.retrofit.database.asset

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.UUID

interface AssetApiService {

    @GET("assets")
    fun getByUserUID(
        @Header("Authorization") token: String,
        @Query("userUID") userUID: UUID
    ): Call<List<Asset>>

    @POST("assets")
    fun createAsset(
        @Header("Authorization") token: String,
        @Body asset: Asset
    ): Call<Long>

}