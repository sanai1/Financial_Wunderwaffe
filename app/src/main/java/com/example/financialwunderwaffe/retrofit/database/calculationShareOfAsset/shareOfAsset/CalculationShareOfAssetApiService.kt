package com.example.financialwunderwaffe.retrofit.database.calculationShareOfAsset.shareOfAsset

import com.example.financialwunderwaffe.retrofit.request.RequestTemplate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.UUID

interface CalculationShareOfAssetApiService {

    @GET("calculation_share_of_asset")
    fun getCalculationShareOfAssetByUID(
        @Header("Authorization") token: String,
        @Query("userUID") userUID: UUID
    ): Call<RequestTemplate<CalculationShareOfAsset>>

}