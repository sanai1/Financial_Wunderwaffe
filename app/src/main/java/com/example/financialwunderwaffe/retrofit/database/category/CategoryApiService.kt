package com.example.financialwunderwaffe.retrofit.database.category

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.util.UUID

interface CategoryApiService {

    @GET("category")
    fun getByUserUID(
        @Header("Authorization") token: String,
        @Query("userUID") userUID: UUID
    ): Call<List<Category>>

    @POST("category")
    fun createCategory(
        @Header("Authorization") token: String,
        @Body category: Category
    ): Call<Long>

    @PUT("category")
    fun updateCategoryBuID(
        @Query("categoryID") categoryID: Long,
        @Query("name") name: String
    ): Call<Boolean>
}