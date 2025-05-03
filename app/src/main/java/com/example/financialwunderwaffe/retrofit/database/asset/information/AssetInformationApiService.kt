package com.example.financialwunderwaffe.retrofit.database.asset.information

import com.example.financialwunderwaffe.retrofit.database.asset.information.model.AssetInformation
import com.example.financialwunderwaffe.retrofit.database.asset.information.model.AssetPrice
import com.example.financialwunderwaffe.retrofit.database.asset.information.model.AssetTransaction
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AssetInformationApiService {

    @GET("assets_information/{asset_id}")
    fun getInformationByAssetId(
        @Header("Authorization") token: String,
        @Path("asset_id") assetId: Long
    ): Call<List<AssetInformation>>

    @POST("assets_information/price")
    fun createAssetPrice(
        @Header("Authorization") token: String,
        @Body assetPrice: AssetPrice
    ): Call<Long>

    @POST("assets_information/transaction")
    fun createAssetTransaction(
        @Header("Authorization") token: String,
        @Body assetTransaction: AssetTransaction
    ): Call<Long>

}